package ui;

import DataObjects.ListGamesRequest;

import java.util.Arrays;

public class PostloginClient {

    private final String serverUrl;
    private final ServerFacade server;
    private State state = State.LOGEDOUT;
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
                case "list" ->list();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

    }


    public String list() throws ResponseException{
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGames
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
