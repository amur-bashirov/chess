package ui;

import chess.ChessGame;
import objects.*;
import model.GameData;

import java.util.Arrays;

public class PostloginClient {

    private final String serverUrl;
    private final ServerFacade server;
    private State state;
    private String authToken = "";
    private int countID = 0;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private String color;
    private ChessGame game;
    private int id;


    public PostloginClient(String serverUrl, State state, String authToken, NotificationHandler notificationHandler ) throws ResponseException {
        this.notificationHandler = notificationHandler;
        this.ws = new WebSocketFacade(serverUrl,notificationHandler);
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = state;
        this.authToken = authToken;
    }

    public String eval (String input,State state, String authToken){
        try {
            this.authToken = authToken;
            this.state = state;
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "join" -> join(params);
                case"observe" -> observe(params);
                case "create" -> create(params);
                case "list" ->list();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

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


    public String logout() throws ResponseException{
        LogoutRequest request = new LogoutRequest(authToken);
        server.logout(request,authToken);
        boolean exception = server.getException();
        if(exception == true){
            return "";
        }
        this.state = State.LOGEDOUT;
        return String.format("You are logged out");
    }

    public String observe(String...params)throws ResponseException{
        if (params.length == 1){
            if (isInteger(params[0])){
                int id = Integer.parseInt(params[0])-1;
                ListGamesRequest request1 = new ListGamesRequest(authToken);
                ListGamesResult result = server.listGames(request1, authToken);
                GameData data = new GameData(0,null,null,null,null);

                if (result.games().isEmpty()){
                    throw new ResponseException(415, "There are no games to observe, dummy");
                }else if(id >= result.games().size() || id < 0){
                    throw new ResponseException(415, "There is no game with this id, dummy");
                } else{
                    data = result.games().get(id);
                }
                String color = "WHITE";
                this.color = color;
                DrawChessBoard.draw(new ChessGame(),null,color);
                state = state.INGAME;
                ws = new WebSocketFacade(serverUrl, notificationHandler);
                game = data.game();
                this.id = id;
                Integer boxedId = data.gameID();
                ws.join(authToken,boxedId);
                return String.format("\nYou are observing the game: %s.", data.gameName());
            }
        }
        throw new ResponseException(415, "\"Incorrect syntax for observe, dummy.\"");
    }

    public String join(String...params) throws ResponseException {
        if (params.length == 2) {
            if (isInteger(params[0]) &&
                    (params[1].equalsIgnoreCase("WHITE") || params[1].equalsIgnoreCase("BLACK"))) {
                int id = Integer.parseInt(params[0])-1;
                String color = params[1];
                ListGamesRequest request1 = new ListGamesRequest(authToken);
                ListGamesResult result = server.listGames(request1, authToken);
                boolean exception = server.getException();
                GameData data = new GameData(0,null,null,null,null);
                if(exception == true){
                    return "";
                }
                if (result.games().isEmpty()){
                    throw new ResponseException(415, "There are no games to join, dummy");
                }else if(id >= result.games().size() || id < 0){
                    throw new ResponseException(415, "There is no game with this id, dummy");
                } else{
                    data = result.games().get(id);
                }
                this.color = color;

                JoinGameRequest request2 = new JoinGameRequest(authToken, color, data.gameID());
                server.joinGame(request2, authToken);
                exception = server.getException();
                if(exception == true){
                    return "";
                }

                state = state.INGAME;
                ws = new WebSocketFacade(serverUrl, notificationHandler);
                game = data.game();
                this.id = id;
                Integer boxedId = data.gameID();
                ws.join(authToken,boxedId);
                return String.format("\nYou joined game as %s.",color);
            }
        }
            throw new ResponseException(415, "\"Incorrect syntax for join, dummy.\"");

    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public ChessGame getGame() {
        return game;
    }

    public String create(String...params) throws ResponseException {
        if (params.length == 1) {
            String gameName = params[0];
            CreateGamesRequest request = new CreateGamesRequest(authToken, gameName);
            CreateGameResult result = server.createGame(request, authToken);
            boolean exception = server.getException();
            if(exception == true){
                return "";
            }
            return String.format("You created game: %s.", gameName);
        }
        throw new ResponseException(415,"\"Incorrect syntax for create, dummy.\"");
    }


    public String list() throws ResponseException{
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request, authToken);
        boolean exception = server.getException();
        if(exception == true){
            return "";
        }
        String result2 = "";
        if (result.games().isEmpty()){
            result2 = "There are no games";
            return (result2);
        }else {
            result2 = "These are available games:\n";
            for (int i = 0; i < result.games().size();i++){
                GameData data = result.games().get(i);
                int id = data.gameID();
                String whiteUser = data.whiteUsername();
                if(whiteUser == null){
                    whiteUser = "available";
                }
                String blackUser = data.blackUsername();
                if(blackUser == null){
                    blackUser = "available";
                }
                this.countID++;
                String gameName = data.gameName();
                result2 += "Game ID: " + countID + "\n";
                result2 += "Game Name: " + gameName + "\n";
                result2 += "White Player: " + whiteUser + "\n";
                result2 += "Black Player: " + blackUser + "\n";
                result2 += "----------------------\n";

            }
            this.countID = 0;
            return result2;
        }
    }

    public State getState() {
        return this.state;
    }

    public String help() {
        String result = "Available commands:\n" +
                "create <Name> - a game\n" +
                "list - games\n" +
                "join <ID> [WHITE][BLACK] - a game\n" +
                "observe <ID> - a game\n" +
                "logout - when you are done\n" +
                "help - with possible commands";
        return result;
    }
}
