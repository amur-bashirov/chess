package server;

public class BadRequestsException extends RuntimeException {
    public BadRequestsException(String message) {
        super(message);
    }
}
