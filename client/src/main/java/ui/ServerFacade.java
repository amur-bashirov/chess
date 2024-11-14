package ui;

import DataObjects.*;
import com.google.gson.Gson;
import DataObjects.OccupiedException;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws ResponseException{
        var path = "/session";
        return makeRequest("POST",path,request,LoginResult.class);
    }

    public void logout(LogoutRequest request) throws ResponseException{
        var path = "/session";
        makeRequest("DELETE",path,request,null);
    }
    public Object CreateGameResult (CreateGamesRequest request) throws ResponseException{
        var path = "/game";
        return makeRequest("POST",path,request,CreateGameResult.class);
    }

    public void listGames(ListGamesRequest request) throws ResponseException{
        var path = "/game";
        makeRequest("GET",path,request,null);
    }
    public void JoinGame(JoinGameRequest request) throws ResponseException{
        var path = "/game";
        makeRequest("PUT",path,request,null);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            System.out.println("ResponseException");
        } catch (IOException ex){
            System.out.println("IOException: " + ex.getMessage());
        } catch (URISyntaxException e) {
            System.out.println("Incorrect Syntax, dummy.");
        } catch (BadRequestsException ex){
            System.out.println("Bad Request, dummy.");
        } catch (OccupiedException ex){
            System.out.println("It is already taken, dummy.");
        } catch(DataAccessException ex){
            System.out.println("It is not accessible, dummy.");
        }
        return null;
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException,
            ResponseException, BadRequestsException, OccupiedException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            if (status == 400) {
                throw new BadRequestsException("Bad Request: " + status);
            }
            if (status == 403){
                throw new OccupiedException("Already occupied" + status);
            }
            if (status == 401){
                throw new DataAccessException("It is not Accessible"+status);
            }
            throw new ResponseException(status, "failure: " + status);
        }
    }



    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
