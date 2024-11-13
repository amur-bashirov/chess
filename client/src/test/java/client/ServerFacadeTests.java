package client;

import DataObjects.*;
import org.junit.jupiter.api.*;
import ui.PreloginClient;
import ui.Repl;
import ui.ResponseException;
import ui.ServerFacade;


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
        OccupiedException exception = Assertions.assertThrows(OccupiedException.class, () -> {
            serverFacade.register(request);
        }, "Expected OccupiedException to be thrown, but it wasn't");
    }

    @Test
    public void successLoginTest() throws ResponseException{
        RegisterRequest request =
                new RegisterRequest("tusername","tpassword","temail");
        RegisterResult result = serverFacade.register(request);
        //LogoutRequest result2 = serverFacade.logout(result.authToken());
    }

}
