package ui;

public class ResponseException extends Exception {
    private int status;
    public ResponseException(int status, String message) {
        super(message);
    }
}
