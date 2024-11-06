package service.dataaccess;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.sql.SqlAuth;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SqlAuthTest {
    AuthDAO sqlauth;

    @BeforeEach
    void setUp() {
        assertDoesNotThrow(() -> {sqlauth = new SqlAuth();});
        UserData ud = new UserData("dummy", "password", "dummy@byu.edu");
        assertDoesNotThrow(() -> {sqlauth.createAuthToken(ud);});
    }

    @AfterEach
    void tearDown() {
        assertDoesNotThrow(() -> {sqlauth.clearTokens();});
    }

    @Test
    void clearTokens() {
        assertDoesNotThrow(() -> {sqlauth.clearTokens();});
    }

    @Test
    void posCreateAuthToken() {
        UserData moreData = new UserData("dumb", "passwordisme", "dumb@byu.edu");
        assertDoesNotThrow(() -> {sqlauth.createAuthToken(moreData);});
    }

    @Test
    void negCreateAuthToken() {
        UserData ud = new UserData(null, "password", "dummy@byu.edu");
        assertThrows(DataAccessException.class, () -> {sqlauth.createAuthToken(ud);});
    }

    @Test
    void posDeleteAuthToken() {
        UserData moreUserData = new UserData("dumb", "passwordisme", "dumb@byu.edu");
        String token = null;
        try {
            token = sqlauth.createAuthToken(moreUserData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String finalToken = token;
        AuthData testAuthData = new AuthData(finalToken,moreUserData.username());
        assertDoesNotThrow(() -> {sqlauth.deleteAuthToken(testAuthData);});
    }

    @Test
    void negDeleteAuthToken() {
        UserData moreUserData = new UserData("dumb", "passwordisme", "dumb@byu.edu");
        String token = null;
        try {
            sqlauth.createAuthToken(moreUserData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        AuthData testAuthData = new AuthData("badToken",moreUserData.username());
        assertDoesNotThrow(() -> {sqlauth.deleteAuthToken(testAuthData);});
    }

    @Test
    void posFindByToken() {
        UserData moreUserData = new UserData("dumb", "passwordisme", "dumb@byu.edu");
        String token = null;
        try {
            token = sqlauth.createAuthToken(moreUserData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String finalToken = token;
        AuthData expectedAuthData = new AuthData(finalToken, moreUserData.username());
        AuthData actualAuthData;
        try {
            actualAuthData = sqlauth.findByToken(finalToken);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedAuthData, actualAuthData);
    }

    @Test
    void negFindByToken() {
    }
}