package server;

public record CreateGamesRequest(String authToken, String gameName) {
}
