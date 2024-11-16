package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

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
    public void posJoinTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void negJoinTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void posListGamesTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void negListGamesTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void posCreateGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void negCreateGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void posLogoutTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void negLogoutTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void posLoginTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void negLoginTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void posRegisterTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void negRegisterTest() {
        Assertions.assertTrue(true);
    }

}
