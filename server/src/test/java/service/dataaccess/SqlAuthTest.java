package service.dataaccess;
import dataaccess.AuthDAO;
import dataaccess.sql.SqlAuth;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlAuthTest {
    AuthDAO sqlauth;

    @BeforeEach
    void setUp() {
        UserData ud = new UserData("dummy", "password", "dummy@byu.edu");
        assertDoesNotThrow(() -> {sqlauth = new SqlAuth();});
        assertDoesNotThrow(() -> {sqlauth.createAuthToken(ud);});
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void clearTokens() {
        assertDoesNotThrow(() -> {sqlauth.clearTokens();});
    }

    @Test
    void posCreateAuthToken() {
    }

    @Test
    void negCreateAuthToken() {
    }

    @Test
    void posDeleteAuthToken() {
    }

    @Test
    void negDeleteAuthToken() {
    }

    @Test
    void posFindByToken() {
    }

    @Test
    void negFindByToken() {
    }
}