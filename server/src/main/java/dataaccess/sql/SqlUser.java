package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlUser implements UserDAO {

    public SqlUser() throws Exception {
        configureDatabase();
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
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
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
        } catch (SQLException e) {
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
        var statement = "INSERT INTO usertable (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, ud.username(), ud.password(), ud.email());
    }


    @Override
    public UserData findByUnPwd(String username, String password) {
        UserData ud = findByUsername(username);
        if (ud.password() == password) {
            return ud;
        } else {
            throw new RuntimeException("Wrong password");
        }
    }

    @Override
    public UserData findByUsername(String username) {
        var statement = "SELECT * FROM usertable WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery(username)) {
                    return readToUserDataObject(rs);
                }
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private UserData readToUserDataObject(ResultSet rs) {
        int gameID = 0;
        String username = null;
        String password = null;
        String email = null;

        try {
            username = rs.getString("username");
            password = rs.getString("password");
            email = rs.getString("email");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new UserData(username, password, email);
    }


    @Override
    public HashMap<String, UserData> getUserList() {
        var result= new HashMap<String, UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gametable";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var myObject = readToUserDataObject(rs);
                        result.put(myObject.username(),myObject);
                    }
                }
            }
        } catch (Exception e) {
//            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
            throw new RuntimeException(e);
        }
        return result;
    }
}
