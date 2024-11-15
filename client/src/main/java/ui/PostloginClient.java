package ui;

import DataObjects.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static ui.EscapeSequences.ERASE_SCREEN;

public class PostloginClient {

    private final String serverUrl;
    private final ServerFacade server;
    private State state;
    private String authToken = "";


    public PostloginClient(String serverUrl, State state, String authToken){
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
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

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
        server.logout(request);
        this.state = State.LOGEDOUT;
        return String.format("You are logged out");
    }

    public String observe(String...params){
        return null;
    }

    public String join(String...params) throws ResponseException {
        if (params.length == 2) {
            if (isInteger(params[0]) && //WILL it throw an error if there is no params?
                    (params[1].equalsIgnoreCase("WHITE") || params[1].equalsIgnoreCase("BLACK"))) {
                int id = Integer.parseInt(params[0]);
                String color = params[1];
                JoinGameRequest request = new JoinGameRequest(authToken, color, id);
                server.joinGame(request);
                if (color.equalsIgnoreCase("WHITE")) {

                }

            }
            throw new ResponseException(415, "\"Incorrect syntax for join, dummy.\"");
        } throw new ResponseException(415, "\"Incorrect syntax for join, dummy.\"");


    }

    public String create(String...params) throws ResponseException {
        if (params.length == 1) {
            String gameName = params[0];
            CreateGamesRequest request = new CreateGamesRequest(authToken, gameName);
            CreateGameResult result = server.createGame(request, authToken);
            return String.format("You created game: %s.", gameName);
        }
        throw new ResponseException(415,"\"Incorrect syntax for create, dummy.\"");
    }


    public String list() throws ResponseException{
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request);
        String result2 = "";
        if (result == null){
            result2 = "There are no games";
        }else {
            result2 = "These are available games: ";
        }
        return (result2 + result.toString());
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
                "quit - playing chess\n" +
                "help - with possible commands";
        return result;
    }
}
