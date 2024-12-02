package ui;

import java.util.Arrays;

public class ChessClient {

    private final String serverUrl;
    private final ServerFacade server;
    private State state;
    private String authToken = "";
    private int countID = 0;


    public ChessClient(String serverUrl, State state, String authToken){
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = state;
        this.authToken = authToken;
    }

    public String eval(String input, State state, String authToken) throws ResponseException{
        this.authToken = authToken;
        this.state = state;
        var tokens = input.toLowerCase().split("");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {

            default -> help();
        };

    }

    public State getState() {
        return state;
    }

    public String getAuthToken() {
        return authToken;
    }

    private String help() {
        String result = "Available commands:\n" +
                "redraw- chess board\n" +
                "leave - the game\n" +
                "make move - in the game\n" +
                "resign  - from the game\n" +
                "highlight - to see the possible moves\n" +
                "help - with possible commands";
        return result;
    }
}
