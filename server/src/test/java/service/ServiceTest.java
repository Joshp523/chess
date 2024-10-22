package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.memory.MemAuth;
import dataaccess.memory.MemGame;
import dataaccess.memory.MemUser;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.function.Executable;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private UserDAO MemUser = new MemUser();
    private AuthDAO MemAuth = new MemAuth();
    private GameDAO MemGame = new MemGame();
    Service service = new Service(MemUser, MemAuth, MemGame);
    ServiceTest(){}

    @BeforeEach
    void setUp() {
        this.service.clear();
        this.service = new Service(MemUser, MemAuth, MemGame);
    }
//
//    @AfterEach
//    void tearDown() {
//
//    }

    @Test
    void clearGood() {
        Assertions.assertDoesNotThrow(() ->service.clear());
    }

    @Test
    void createUserGood() {
        Assertions.assertDoesNotThrow(() ->service.createUser(new UserData("Ben","password","cosmo@byu.edu")));
    }

    @Test
    void createUserBad() {
        Assertions.assertDoesNotThrow(() ->service.createUser(new UserData("Ben","password","cosmo@byu.edu")));
        Assertions.assertThrows(DataAccessException.class,() ->service.createUser(new UserData("Ben","password","cosmo@byu.edu")));
    }

    @Test
    void createAuthTokenGood() {
        Assertions.assertDoesNotThrow(() ->service.createUser(new UserData("Ben","password","cosmo@byu.edu")));
        Assertions.assertDoesNotThrow(() ->service.createAuthToken(new UserData("Ben","password","cosmo@byu.edu")));

    }

    @Test
    void createAuthTokenBad() {
        this.service.clear();
        Assertions.assertThrows(DataAccessException.class,
                () ->service.createAuthToken(new UserData(null,"password","cosmo@byu.edu")));
    }

    @Test
    void findDataByUnPwdGood() throws DataAccessException {
        UserData expected = new UserData("Ben","benpassword","cosmo@byu.edu");
        Assertions.assertDoesNotThrow(() ->service.createUser(expected));
        UserData actual = service.findDataByUnPwd("Ben", "benpassword");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findDataByUnPwdBad() throws DataAccessException {
        UserData expected = new UserData("Ben","benpassword","cosmo@byu.edu");
        Assertions.assertDoesNotThrow(() ->service.createUser(expected));
        Assertions.assertThrows(DataAccessException.class, () ->service.findDataByUnPwd("Ben", "badPassword"));

    }

    @Test
    void logoutGood() throws DataAccessException {
        UserData expected = new UserData("Ben","benpassword","cosmo@byu.edu");
        Assertions.assertDoesNotThrow(() ->service.createUser(expected));
        String tok = service.createAuthToken(expected);
        Assertions.assertDoesNotThrow(() ->service.logout(tok));
    }

    @Test
    void logoutBad() throws DataAccessException{
        UserData expected = new UserData("Ben","benpassword","cosmo@byu.edu");
        Assertions.assertDoesNotThrow(() ->service.createUser(expected));
        service.createAuthToken(expected);
        Assertions.assertThrows(DataAccessException.class, () ->service.logout("wrongToken"));
    }

    @Test
    void validTokenGood() throws DataAccessException {
        UserData user = new UserData("Ben","benpassword","cosmo@byu.edu");
        service.createUser(user);
        String tok = service.createAuthToken(user);
        Assertions.assertTrue(() -> service.validToken(tok));

    }

    @Test
    void validTokenBad() {
        Assertions.assertFalse(() ->service.validToken("wrongToken"));
    }

    @Test
    void listGamesGood() throws DataAccessException {
        service.createGame("first");
        service.createGame("second");
        int expected = 2;
        int actual = service.listGames().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void listGamesBad() throws DataAccessException {
        int expected = 0;
        int actual = service.listGames().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createGameGood() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> service.createGame("first"));
    }

    @Test
    void createGameBad() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> service.createGame(""));
        service.createGame("first");
        service.createGame("second");
        Assertions.assertThrows(DataAccessException.class, () -> service.createGame("second"));
    }

    @Test
    void joinGameGood() throws DataAccessException {
        int id = service.createGame("first");
        service.createUser(new UserData("Ben","benpassword","cosmo@byu.edu"));
        service.createUser(new UserData("Josh","joshpassword","shane@byu.edu"));
        String jToken = service.createAuthToken(new UserData("Josh","joshpassword","shane@byu.edu"));
        String bToken = service.createAuthToken(new UserData("Ben","benpassword","cosmo@byu.edu"));
        Assertions.assertDoesNotThrow(() -> service.joinGame(jToken, WHITE, id));
        Assertions.assertDoesNotThrow(() -> service.joinGame(bToken, BLACK, id));
    }

    @Test
    void joinGameBad() throws DataAccessException {
        int id = service.createGame("first");
        service.createUser(new UserData("Ben","benpassword","cosmo@byu.edu"));
        service.createUser(new UserData("Josh","joshpassword","shane@byu.edu"));
        String jToken = service.createAuthToken(new UserData("Josh","joshpassword","shane@byu.edu"));
        String bToken = service.createAuthToken(new UserData("Ben","benpassword","cosmo@byu.edu"));
        Assertions.assertThrows(Exception.class, () -> service.joinGame("bad token", WHITE, id));
        Assertions.assertDoesNotThrow(() -> service.joinGame(bToken, BLACK, id));
        Assertions.assertThrows(Exception.class, () -> service.joinGame(bToken, BLACK, id));
    }
}