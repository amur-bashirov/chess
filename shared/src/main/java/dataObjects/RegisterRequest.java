package dataObjects;

public record RegisterRequest(
        String username,
        String password,
        String email)
{}
