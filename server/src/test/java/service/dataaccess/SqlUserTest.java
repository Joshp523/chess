package service.dataaccess;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlUser;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlUserTest {
    static UserDAO sqluser;
    static UserData ud;

    @BeforeEach
     void setUp() {
        assertDoesNotThrow(() -> {
            sqluser = new SqlUser();
        });
        assertDoesNotThrow(() -> {
            sqluser.clearUsers();
        });
        ud = new UserData("dummy", "password", "dummy@byu.edu");
        assertDoesNotThrow(() -> {
            sqluser.createUser(ud);
        });
    }

@AfterEach
void tearDown() {
    assertDoesNotThrow(() -> {
        sqluser.clearUsers();
    });
}

@Test
void clearUsers() {
    assertDoesNotThrow(() -> {
        sqluser.clearUsers();
    });
}

@Test
void posCreateUser() {
    UserData ud2 = new UserData("dumber", "otherpassword", "dumber@byu.edu");
    assertDoesNotThrow(() -> {
        sqluser.createUser(ud2);
    });
}

@Test
void negCreateUser() {
    UserData ud3 = new UserData("dummy", "otherpassword", "dumber@byu.edu");
    assertThrows(DataAccessException.class, () -> {
        sqluser.createUser(ud3);
    });
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