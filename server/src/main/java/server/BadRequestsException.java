package server;

public class BadRequestsException extends Exception {
    public BadRequestsException(String message) {
        super(message);
    }
}
