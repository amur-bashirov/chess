package ui;

import chess.ChessGame;
import chess.ChessPosition;


import java.util.Arrays;

public class ChessClient {

    private final String serverUrl;
    private final ServerFacade server;
    private State state;
    private String authToken = "";
    private int countID = 0;
    private ChessGame game;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private String color;


    public ChessClient(String serverUrl, State state, String authToken, WebSocketFacade ws, String color, ChessGame game)
            throws ResponseException {
        this.game = game;
        this.color = color;
        this.ws = ws;
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = state;
        this.authToken = authToken;
    }

    public String eval(String input, State state, String authToken, String color, ChessGame game) throws ResponseException{
        this.game = game;
        this.color = color;
        this.authToken = authToken;
        this.state = state;
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case"redraw" -> redraw();
            case"help" -> help();
            case "make move" ->  makeMove(params);
            case "leave" -> leave();
            case"resign" -> resign();
            case"highlight" -> highlight(params);
            default -> help();
        };

    }

    public WebSocketFacade getWs() {
        return ws;
    }

    private boolean isInteger(String str) {
        try {
            int value = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String highlight(String...params) throws ResponseException {
        int length = params.length;
        if (params.length == 2) {
            if (isInteger(params[0]) && isInteger(params[1])){
                int row = Integer.parseInt(params[0]);
                int col = Integer.parseInt(params[1]);
                if(row>0 && row <9 && col >0 && col <9){
                    ChessPosition position = new ChessPosition(row,col);
                    DrawChessBoard.draw(game,position,color);
                    String result = "\nHere are the possible moves";
                    return result;
                } throw new ResponseException(415, "Integers are out of range of chess board");
            } throw new ResponseException(415,"Both must be integers");
        }throw new ResponseException(415,"There are no <ROW> <COLUMN>");

    }

    private String redraw() {
        String result = "";
        return result;
    }

    private String resign() {
        String result = "";
        return result;
    }

    private String leave() {
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
                "make move from <ROW> <COLUMN> to <ROW> <COLUMN>- in the game\n" +
                "resign  - from the game\n" +
                "highlight <ROW> <COLUMN> - to see the possible moves\n" +
                "help - with possible commands";
        return result;
    }
}
