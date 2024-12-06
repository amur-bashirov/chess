package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import objects.ListGamesRequest;
import objects.ListGamesResult;


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
    private int id;


    public ChessClient(String serverUrl, State state, String authToken,
                       WebSocketFacade ws, String color, ChessGame game, int id, NotificationHandler notificationHandler)
            throws ResponseException {
        this.id = id;
        this.game = game;
        this.color = color;
        this.notificationHandler = notificationHandler;
        this.ws = new WebSocketFacade(serverUrl,notificationHandler);
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
            case "make" ->  makeMove(params);
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
        DrawChessBoard.draw(game,null,color);
        String result = "\nHere is redrawn board";
        return result;
    }

    private String resign() throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ListGamesRequest request1 = new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request1, authToken);
        Integer boxedId = result.games().get(id).gameID();
        String message = "You resigned from the game";
        ws.resign(authToken,boxedId);
        return message;
    }

    private String leave() throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ListGamesRequest request1 = new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request1, authToken);
        Integer boxedId = result.games().get(id).gameID();
        String message = "You left from the game";
        ws.leave(authToken,boxedId);
        state = state.LOGEDIN;
        return message;
    }

    private String makeMove(String[] params) throws ResponseException {
        int length = params.length;
        if(params.length==0){
            return help();
        }
        if(!params[0].equals("move")){
            return help();
        }
        if (params.length == 5) {
            if (isInteger(params[1]) && isInteger(params[2]) && isInteger(params[3]) && isInteger(params[4])){
                int row = Integer.parseInt(params[1]);
                int col = Integer.parseInt(params[2]);
                int row2 = Integer.parseInt(params[3]);
                int col2 = Integer.parseInt(params[4]);
                if(row>0 && row <9 && col >0 && col <9 &&
                        row2>0 && row2 <9 && col2 >0 && col2 <9){
                    ChessPosition startPosition = new ChessPosition(row, col);
                    ChessPosition endPosition = new ChessPosition(row2, col2);
                    ListGamesRequest request1 = new ListGamesRequest(authToken);
                    ListGamesResult result = server.listGames(request1, authToken);
                    Integer boxedId = result.games().get(id).gameID();
                    ws = new WebSocketFacade(serverUrl, notificationHandler);
                    ChessMove move = new ChessMove(startPosition,endPosition,null);
                    ws.makeMove(authToken, boxedId,move);
                    String message = "\nYou made the move";
                    return message;
                } throw new ResponseException(415, "Try again, numbers are not real");
            } throw new ResponseException(415,"All four must be integers");
        }throw new ResponseException(415,"There are no <ROW> <COLUMN> <ROW> <COLUMN>");
    }

    public State getState() {
        return state;
    }

    public ChessGame getGame() {
        return game;
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
