package ui;

import ui.websocket.NotificationHandler;

import java.util.Arrays;

public class PreloginClient {

    private final ServerFacade server;
    private final String serverUrl;
    private final State state = State.SIGNEDOUT;


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

    }
}
