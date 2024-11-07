package service.dataaccess;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlGame;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlGameTest {
    static GameDAO sqlgame;
    UserData firstUser;
    UserData secondUser;

    @BeforeEach
    void setUp() {
        assertDoesNotThrow(() -> {
            sqlgame = new SqlGame();
        });
        UserData firstUser = new UserData("dumb", "password", "dummy@byu.edu");
        UserData secondUser = new UserData("dumber", "dumberpassword", "dummyer@byu.edu");
    }

    @AfterEach
    void tearDown() {
        assertDoesNotThrow(() -> {
            sqlgame.clearGames();
        });
    }

    @Test
    void posClearGames() {
        assertDoesNotThrow(() -> {
            sqlgame.clearGames();
        });
    }

    @Test
    void posCreateGame() {
        assertDoesNotThrow(() -> {sqlgame.createGame("cool new game");});
    }

    @Test
    void negCreateGame() {
        assertDoesNotThrow(() -> {sqlgame.createGame("cool new game");});
        assertThrows(DataAccessException.class, () -> {sqlgame.createGame("cool new game");});
    }

    @Test
    void posGetAllGames() {
        assertDoesNotThrow(() -> {sqlgame.createGame("cool new game");});
        assertDoesNotThrow(() -> {sqlgame.createGame("kinda cool game");});
        assertDoesNotThrow(() -> {sqlgame.createGame("silly game");});
        assertEquals(3, sqlgame.getAllGames().size());
    }

    @Test
    void negGetAllGames() {
        assertDoesNotThrow(() -> {sqlgame.createGame("cool new game");});
        assertDoesNotThrow(() -> {sqlgame.createGame("kinda cool game");});
        assertDoesNotThrow(() -> {sqlgame.createGame("silly game");});
        assertNotEquals(0, sqlgame.getAllGames().size());
    }

    @Test
    void posAddUser() {
    }

    @Test
    void negAddUser() {
    }
}