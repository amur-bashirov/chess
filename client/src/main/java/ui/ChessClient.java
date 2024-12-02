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

    public String eval (String input,State state, String authToken) throws ResponseException{
        this.authToken = authToken;
        this.state = state;
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {

            default -> help();
        };

    }

    private String help() {
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
