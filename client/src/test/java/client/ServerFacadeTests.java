package client;

import DataObjects.*;
import Server.Server;
import org.junit.jupiter.api.*;
import ui.ResponseException;
import ui.ServerFacade;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ServerFacadeTests {



    private static ServerFacade serverFacade;
    private static Server server;

    private static final String serverUrl = "http://localhost:8080";

    private String existingAuth;

    @AfterAll
    static void stopServer() {

        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new ServerFacade(serverUrl);
    }






    @Test
    public void successRegisterTest() throws ResponseException {
        RegisterRequest request =
                new RegisterRequest("tusername","tpassword","temail");
        RegisterResult result = serverFacade.register(request);
        Assertions.assertNotNull(result, "ServerFacade returned an empty file");
        Assertions.assertEquals(request.username(),result.username(),"different usernames");
        Assertions.assertNotNull(result.authToken(),"authToken is empty");
    }

    @Test
    public void failureRegisterTest()throws ResponseException{
        RegisterRequest request =
                new RegisterRequest("tusername","tpassword","temail");
        RegisterResult result = serverFacade.register(request);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            serverFacade.register(request);
        } catch (ResponseException ignored) {
        }

        System.setOut(originalOut);

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("It is already taken, dummy."),
                "Expected message was not printed.");
    }

    @Test
    public void successLoginTest() throws ResponseException{
        RegisterRequest request =
                new RegisterRequest("tusername","tpassword","temail");
        RegisterResult result = serverFacade.register(request);
        LogoutRequest request2 = new LogoutRequest(result.authToken());
        serverFacade.logout(request2,result.authToken());
        LoginRequest request3 = new LoginRequest(request.username(),request.password());
        LoginResult result2 = serverFacade.login(request3);
        Assertions.assertNotNull(result2,"login method return empty file");
    }

    @Test
    public void failureLoginTest() throws ResponseException{
        RegisterRequest request =
                new RegisterRequest("tusername","tpassword","temail");
        RegisterResult result = serverFacade.register(request);
        LogoutRequest logoutRequest = new LogoutRequest(result.authToken());
        serverFacade.logout(logoutRequest,result.authToken());
        LoginRequest invalidLoginRequest = new LoginRequest("tusername", "wrongpassword");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            serverFacade.login(invalidLoginRequest);
            Assertions.fail("Expected ResponseException was not thrown");
        } catch (ResponseException ex) {
            String output = outputStream.toString();
            Assertions.assertTrue(output.contains("ResponseException"), "Expected exception message not printed.");
        }
        System.setOut(originalOut);

    }

    @Test
    public void successLogoutTest() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("user", "password", "user@example.com");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        LogoutRequest logoutRequest = new LogoutRequest(registerResult.authToken());
        serverFacade.logout(logoutRequest,registerResult.authToken());
        Assertions.assertTrue(true);
    }

    @Test
    public void failureLogoutTest() throws ResponseException {
        LogoutRequest invalidLogoutRequest = new LogoutRequest("invalid-token");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            serverFacade.logout(invalidLogoutRequest,"invalid-token");
        } catch (ResponseException ignored) {
        }

        System.setOut(originalOut);

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("It is not accessible, dummy."),
                "Expected 'It is not accessible, dummy.' message not printed.");
    }

    @Test
    public void successCreateGameTest() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tusername", "tpassword", "temail");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        CreateGamesRequest createGameRequest = new CreateGamesRequest(registerResult.authToken(), "game description");
        CreateGameResult result = serverFacade.createGame(createGameRequest,registerResult.authToken());


        Assertions.assertNotNull(result, "Game creation failed.");
    }

    @Test
    public void failureCreateGameTest() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tusername", "tpassword", "temail");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        CreateGamesRequest invalidCreateGameRequest = new CreateGamesRequest("", "description");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            serverFacade.createGame(invalidCreateGameRequest,"random");
        } catch (ResponseException ignored) {
        }
        System.setOut(originalOut);

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("It is not accessible, dummy."),
                "Expected 'It is not accessible, dummy.' message not printed.");
    }

    // Test listGames method
    @Test
    public void successListGamesTest() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tusername", "tpassword", "temail");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        ListGamesRequest listGamesRequest = new ListGamesRequest(registerResult.authToken());
        ListGamesResult result = serverFacade.listGames(listGamesRequest,registerResult.authToken());
        Assertions.assertNotNull(result, "Failed to retrieve games list.");
        Assertions.assertFalse(result.games().isEmpty(), "Games list is empty.");
    }

    @Test
    public void failureListGamesTest() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tusername", "tpassword", "temail");
        RegisterResult registerResult = serverFacade.register(registerRequest);

        ListGamesRequest invalidListGamesRequest = new ListGamesRequest("random");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            serverFacade.listGames(invalidListGamesRequest,"random");
        } catch (ResponseException ignored) {
        }
        System.setOut(originalOut);

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("It is not accessible, dummy."),
                "Expected 'It is not accessible, dummy.' message not printed.");
    }


    @Test
    public void successJoinGameTest() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tusername", "tpassword", "temail");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        CreateGamesRequest createGameRequest = new CreateGamesRequest(registerResult.authToken(), "description");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest, registerResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(),"black",9);
        serverFacade.joinGame(joinGameRequest,registerResult.authToken());
        Assertions.assertTrue(true);
    }

    @Test
    public void failureJoinGameTest() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tusername", "tpassword", "temail");
        RegisterResult registerResult = serverFacade.register(registerRequest);
        JoinGameRequest invalidJoinGameRequest = new JoinGameRequest("invalid-game-id", "WHITE", 8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            serverFacade.joinGame(invalidJoinGameRequest,"invalid-game-id");
        } catch (ResponseException ignored) {
        }
        System.setOut(originalOut);
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("It is not accessible, dummy."),
                "Expected 'It is not accessible, dummy.' message not printed.");
    }

}
