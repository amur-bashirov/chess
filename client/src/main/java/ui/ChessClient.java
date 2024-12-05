package ui;

import chess.ChessGame;

import java.util.Arrays;

public class ChessClient {

    private final String serverUrl;
    private final ServerFacade server;
    private State state;
    private String authToken = "";
    private int countID = 0;
    private ChessGame game;


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
            case"redraw" -> redraw(params);
            case"help" -> help();
            case "make move" ->  makeMove(params);
            case "leave" -> leave(params);
            case"resign" -> resign(params);
            case"highlight" -> hightlight(params);
            default -> help();
        };

    }

    private String hightlight(String[] params) {
        String result = "";
        return result;
    }

    private String redraw(String[] params) {
        String result = "";
        return result;
    }

    private String resign(String[] params) {
        String result = "";
        return result;
    }

    private String leave(String[] params) {
        String result = "";
        return result;
    }

    private String makeMove(String[] params) {
        String result = "";
        return result;
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
