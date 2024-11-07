package service.dataaccess;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlUser;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SqlUserTest {
    static UserDAO sqluser;
    static UserData ud;
    static UserData ud1;

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
        ud1 = new UserData("dumb", "passworder", "dumb@byu.edu");
        assertDoesNotThrow(() -> {
            sqluser.createUser(ud1);
        });
    }

@AfterAll
static void tearDown() {
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
    UserData renderedUserData;
    try {
        renderedUserData = sqluser.findByUnPwd("dummy", "password");
    } catch (DataAccessException e) {
        throw new RuntimeException(e);
    }
    assertEquals(ud.email(), renderedUserData.email());
}

@Test
void negFindByUnPwd() {
    assertThrows(DataAccessException.class, () -> {sqluser.findByUnPwd("dummy", "wrongpassword");});
}

@Test
void posFindByUsername() {
    UserData renderedUserData;
    try {
        renderedUserData = sqluser.findByUsername("dummy");
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    assertEquals(ud.email(), renderedUserData.email());
}

@Test
void negFindByUsername() {
    assertNull(sqluser.findByUsername("wrong username"));
}

@Test
void posGetUserList() {
        assertEquals(2, sqluser.getUserList().size());
}

@Test
void negGetUserList() {
    assertNotEquals(0, sqluser.getUserList().size());
}
}