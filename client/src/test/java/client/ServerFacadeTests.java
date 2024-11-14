package client;

import DataObjects.*;
import org.junit.jupiter.api.*;
import ui.PreloginClient;
import ui.Repl;
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
            // Second call should print the message "It is already taken, dummy."
            serverFacade.register(request);
        } catch (ResponseException ignored) {
            // Exception will be handled internally, so we ignore it here
        }

        // Reset System.out
        System.setOut(originalOut);

        // Check if the expected message was printed
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
        serverFacade.logout(request2);
        LoginRequest request3 = new LoginRequest(request.username(),request.password());
        LoginResult result2 = serverFacade.login(request3);
        Assertions.assertNotNull(result2,"login method return empty file");
    }

}
