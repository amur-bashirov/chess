package ui;

import DataObjects.*;

import java.util.Arrays;

public class PostloginClient {

    private final String serverUrl;
    private final ServerFacade server;
    private State state = State.LOGEDIN;
    private String authToken = "";

    public PostloginClient(String serverUrl){
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval (String input){
        try {
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

    public String logout() throws ResponseException{
        LogoutRequest request = new LogoutRequest(authToken);
        server.logout(request);
        this.state = State.LOGEDOUT;
        return String.format("You are logged out");
    }

    public String observe(String...params){
        return null;
    }

    public String join(String...params){
        return null;
    }

    public String create(String...params) throws ResponseException {
        String gameName = params[0];
        CreateGamesRequest request = new CreateGamesRequest(authToken,gameName);
        CreateGameResult result = server.createGame(request);
        return String.format("You created game: %s.", gameName);
    }


    public String list() throws ResponseException{
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request);
        String result2 = "These are the available games: ";
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
