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
    private boolean exception = false;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, request, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest request) throws ResponseException{
        var path = "/session";
        return makeRequest("POST",path,request,LoginResult.class, null);
    }

    public void logout(LogoutRequest request, String authToken) throws ResponseException{
        var path = "/session";
        makeRequest("DELETE",path,request,null, authToken);
    }
    public CreateGameResult createGame(CreateGamesRequest request, String authToken) throws ResponseException{
        var path = "/game";
        return makeRequest("POST",path,request,CreateGameResult.class, authToken);
    }

    public ListGamesResult listGames(ListGamesRequest request, String authToken) throws ResponseException{
        var path = "/game";
        return makeRequest("GET",path,null,ListGamesResult.class, authToken);
    }
    public void joinGame(JoinGameRequest request, String authToken) throws ResponseException{
        var path = "/game";
        makeRequest("PUT",path,request,null, authToken);
    }

    public boolean getException() {
        return exception;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        this.exception = false;
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeHeader(authToken,http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            System.out.println("ResponseException");
            this.exception = true;
        } catch (IOException ex){
            System.out.println("IOException: " + ex.getMessage());
            this.exception = true;
        } catch (URISyntaxException e) {
            System.out.println("Incorrect Syntax, dummy.");
            this.exception = true;
        } catch (BadRequestsException ex){
            System.out.println("Bad Request, dummy.");
            this.exception = true;
        } catch (OccupiedException ex) {
            System.out.println("It is already taken, dummy.");
            this.exception = true;

        } catch(Unauthorized ex){
            System.out.println("It is not accessible, dummy.");
            this.exception = true;
        }
        return null;
    }

    private static void writeHeader(String authToken, HttpURLConnection http) throws IOException{
        if (authToken != null){
            http.addRequestProperty("Authorization",authToken);
        }
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
            ResponseException, BadRequestsException, OccupiedException, Unauthorized{
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            if (status == 400) {
                throw new BadRequestsException("Bad Request: " + status);
            }
            if (status == 403){
                throw new OccupiedException("Already occupied" + status);
            }
            if (status == 401){
                throw new Unauthorized("It is not Accessible"+status);
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
