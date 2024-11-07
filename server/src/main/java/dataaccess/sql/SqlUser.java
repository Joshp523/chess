package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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
            CREATE TABLE IF NOT EXISTS  Usertable (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            ,
            """
            
            ALTER TABLE usertable ADD CONSTRAINT unique_username UNIQUE (username);
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                if (statement.contains("ALTER TABLE")) {
                    continue;
                }
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                        //else if (param instanceof  p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public void clearUsers() {
        var statement = "TRUNCATE usertable";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData ud) throws DataAccessException {
        try {
            String hashedPwd = BCrypt.hashpw(ud.password(), BCrypt.gensalt());
            var statement = "INSERT INTO usertable (username, password, email) VALUES (?, ?, ?)";
            executeUpdate(statement, ud.username(), hashedPwd, ud.email());
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
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var myObject = readToUserDataObject(rs);
                        result.put(myObject.username(), myObject);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
