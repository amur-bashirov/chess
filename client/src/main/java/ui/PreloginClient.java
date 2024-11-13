package ui;

import DataObjects.LoginRequest;
import DataObjects.LoginResult;
import DataObjects.RegisterRequest;
import DataObjects.RegisterResult;


import java.util.Arrays;

public class PreloginClient {

    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;


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
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

    }

    public String register(String...params) throws ResponseException {
        if (params.length < 3) {
            String userName = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest request = new RegisterRequest(userName, password, email);
            RegisterResult result = server.register(request);
            LoginRequest logRequest = new LoginRequest(result.username(),password);
            LoginResult logResult = server.login(logRequest);
            this.state = State.SIGNEDIN;
            return String.format("You signed in as %s.", userName);
        }
        throw new ResponseException(400,"Expected:<USERNAME> <PASSWORD> <EMAIL>");

    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - logIn <USERNAME> <PASSWORD>
                    - quit
                    -register <USERNAME> <PASSWORD> <EMAIL>
                    """;
        }
        return """
                -I AM NOT SURE WHAT IT DOES
                
                """;
    }
}
