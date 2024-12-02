package ui;

import objects.LoginRequest;
import objects.LoginResult;
import objects.RegisterRequest;
import objects.RegisterResult;


import java.util.Arrays;

public class PreloginClient {

    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGEDOUT;
    private String authToken = "";


    public PreloginClient(String serverUrl){
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval (String input){
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String...params) throws ResponseException {
        if (params.length == 3) {
            String userName = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest request = new RegisterRequest(userName, password, email);
            RegisterResult result = server.register(request);
            boolean exception = server.getException();
            if(exception == true){
                return "";
            }
            if (result != null) {
                authToken = result.authToken();
                this.state = State.LOGEDIN;
                return String.format("You are registered in as %s.", userName);
            }

        }
        this.state = State.LOGEDOUT;
        throw new ResponseException(415,"\"Expected:<USERNAME> <PASSWORD> <EMAIL>, dummy.\"");
    }

    public State getState() {
        return this.state;
    }

    public String getAuthToken(){
        return this.authToken;
    }

    public String login(String...params)throws ResponseException{
        if (params.length == 2){
                String userName = params[0];
                String password = params[1];
                LoginRequest logRequest = new LoginRequest(userName, password);
                LoginResult logResult = server.login(logRequest);
                boolean exception = server.getException();
                if(exception == true){
                    return "";
                }
                if (logResult != null) {
                    this.state = State.LOGEDIN;
                    authToken = logResult.authToken();
                    return String.format("You logged in as %s.", userName);
                }
        }
        this.state = State.LOGEDOUT;
        throw new ResponseException(415,"Expected <USERNAME> <PASSWORD>, dummy.");
    }

    public String help() {
        String result = "Available commands:\n" +
                "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n" +
                "login <USERNAME> <PASSWORD> - to play chess\n" +
                "quit - playing chess\n"+
                "help - with possible commands";
        return result;
    }
}
