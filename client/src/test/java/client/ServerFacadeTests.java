package client;

import chess.ChessGame;
import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlGame;
import dataaccess.sql.SqlUser;
import org.junit.jupiter.api.*;
import server.Server;
import service.Service;
import ui.ServerFacade;


public class ServerFacadeTests {
    private static Service service;

    static {
        try {
            service = new Service(new SqlUser(), new SqlAuth(), new SqlGame());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Server server;
    private static ServerFacade facade;
    //private static ServerFacade facade2;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        String token = facade.register("username", "password", "email");
        facade = new ServerFacade("http://localhost:" + port);
        facade.setTok(token);
        //facade2 = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
        service.clear();
    }


    @Test
    public void posJoinTest() {
        Assertions.assertDoesNotThrow(() -> facade.join(ChessGame.TeamColor.BLACK, 1));
    }

    @Test
    public void negJoinTest() {
        int badID = 300;
        Assertions.assertThrows(Exception.class, () -> {facade.join(ChessGame.TeamColor.BLACK, badID);});
    }

    @Test
    public void posListGamesTest() {
        int size = service.listGames().size();
        Assertions.assertTrue(size> 0);
    }

    @Test
    public void negListGamesTest() {
        service.clear();
        int size = service.listGames().size();
        Assertions.assertEquals(0, size);
    }

    @Test
    public void posCreateGameTest() {
        Assertions.assertDoesNotThrow(() -> facade.createGame("testgame"));
    }

    @Test
    public void negCreateGameTest() {
        Assertions.assertThrows(Exception.class, () -> facade.createGame("testgame"));
    }

    @Test
    public void posLogoutTest() {
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void negLogoutTest() {
        Assertions.assertDoesNotThrow(() -> facade.logout());
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void posLoginTest() {
        Assertions.assertDoesNotThrow(() -> facade.login("username", "password"));
    }

    @Test
    public void negLoginTest() {
        Assertions.assertThrows(Exception.class, () -> {facade.login("username", "wrongpassword");});
    }

    @Test
    public void posRegisterTest() {
        var result = facade.register("otherusername", "password", "email");
        Assertions.assertInstanceOf(String.class, result);
    }

    @Test
    public void negRegisterTest() {
        Assertions.assertThrows(Exception.class, () -> facade.register(null, "password", "email"));
    }

}
