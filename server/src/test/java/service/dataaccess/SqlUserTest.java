package service.dataaccess;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlUser;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SqlUserTest {
    UserDAO sqluser;

    @BeforeEach
    void setUp() {
        assertDoesNotThrow(() -> {
            sqluser = new SqlUser();
        });
        UserData ud = new UserData("dummy", "password", "dummy@byu.edu");
    }

    @AfterEach
    void tearDown() {
        assertDoesNotThrow(() -> {
            sqluser.clearUsers();
        });
    }

    @Test
    void clearUsers() {
    }

    @Test
    void posCreateUser() {
    }

    @Test
    void negCreateUser() {
    }

    @Test
    void posFindByUnPwd() {
    }

    @Test
    void negFindByUnPwd() {
    }

    @Test
    void posFindByUsername() {
    }

    @Test
    void negFindByUsername() {
    }

    @Test
    void posGetUserList() {
    }

    @Test
    void negGetUserList() {
    }
}