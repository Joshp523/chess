package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlUser implements UserDAO {

    public SqlUser() throws Exception {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  usertable (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.makeStatement(createStatements);
    }

    @Override
    public void clearUsers() {
        var statement = "TRUNCATE usertable";
        try (
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement)){
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData ud) throws DataAccessException {
        String hashedPwd = BCrypt.hashpw(ud.password(), BCrypt.gensalt());
        var statement = "INSERT INTO usertable (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(statement))
        {
            ps.setString(1, ud.username());
            ps.setString(2, hashedPwd);
            ps.setString(3, ud.email());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Error: already taken");
        }
    }


    @Override
    public UserData findByUnPwd(String username, String password) throws DataAccessException {
        UserData checkMe = findByUsername(username);
        if (checkMe != null && BCrypt.checkpw(password, checkMe.password())) {
            return checkMe;
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public UserData findByUsername(String username) {
        var statement = "SELECT * FROM usertable WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            return getUserData(username, conn, statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserData getUserData(String username, Connection conn, String statement) throws SQLException {
        try (var ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readToUserDataObject(rs);
                } else {
                    return null;
                }
            }
        }
    }

    private UserData readToUserDataObject(ResultSet rs) {
        String username = null;
        String password = null;
        String email = null;

        try {
            username = rs.getString("username");
            password = rs.getString("password");
            email = rs.getString("email");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new UserData(username, password, email);
    }


    @Override
    public HashMap<String, UserData> getUserList() {
        var result = new HashMap<String, UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM usertable";
            extractedLogic(conn, statement, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void extractedLogic(Connection conn, String statement, HashMap<String, UserData> result) throws SQLException {
        try (var ps = conn.prepareStatement(statement)) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var myObject = readToUserDataObject(rs);
                    result.put(myObject.username(), myObject);
                }
            }
        }
    }
}
