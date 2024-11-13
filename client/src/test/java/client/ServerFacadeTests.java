package client;

import org.junit.jupiter.api.*;
import DataObjects.Server;
import ui.PreloginClient;
import ui.Repl;


public class ServerFacadeTests {

    private static Server server;
    private Repl repl;
    static PreloginClient prelogClient;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }



    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void successRegisterTest() {

    }

}
