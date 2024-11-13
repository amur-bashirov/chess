package ui;

import DataObjects.LoginRequest;
import DataObjects.LoginResult;
import DataObjects.RegisterRequest;
import DataObjects.RegisterResult;


import java.util.Arrays;

import static java.awt.Color.ORANGE;

public class PreloginClient {

    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGEDOUT;


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
            LoginRequest logRequest = new LoginRequest(result.username(),password);
            LoginResult logResult = server.login(logRequest);
            this.state = State.LOGEDIN;
            return String.format("You signed in as %s.", userName);
        }
        System.out.println("Expected:<USERNAME> <PASSWORD> <EMAIL>, dummy.");
        return null;
    }

    public String login(String...params)throws ResponseException{
        if (params.length == 2){
            if (state.equals(State.LOGEDIN)) {
                String userName = params[0];
                String password = params[1];
                LoginRequest logRequest = new LoginRequest(userName, password);
                LoginResult logResult = server.login(logRequest);
                this.state = State.LOGEDIN;
            }
            String result = "You are already logged in, dummy.";
            System.out.println(ORANGE + result);
            return null;
        }
        String result = "Expected <USERNAME> <PASSWORD>, dummy.";
        System.out.println(ORANGE + result);
        return null;
    }

    public String help() {
        String result = "Available commands:\n" +
                "register <USERNAME> <PASSWORD> <EMAIL> - to create commands\n" +
                "login <USERNAME> <PASSWORD> - to play chess\n" +
                "quit - playing chess\n"+
                "help - with possible commands";
        return result;
    }
}
