package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlAuth implements AuthDAO {

    public SqlAuth() throws Exception {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authtable (
              `username` varchar(256) NOT NULL,
              `authtoken` varchar(256) NOT NULL,
              PRIMARY KEY (`authtoken`),
              INDEX(`username`)
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
    public void clearTokens() {
        var statement = "TRUNCATE authtable";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createAuthToken(UserData ud) throws DataAccessException {
        if (ud.username() == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        var statement = "INSERT INTO authtable (username, authtoken) VALUES (?, ?)";
        String newToken = UUID.randomUUID().toString();
        executeUpdate(statement, ud.username(), newToken);
        return newToken;

    }


    @Override
    public void deleteAuthToken(AuthData ad) throws DataAccessException {
        boolean foundFlag = false;
        String deleteMe = ad.authToken();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM authtable WHERE authtoken=?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery(deleteMe)) {
                    while (rs.next()) {
                        foundFlag = true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (foundFlag == false) {
            throw new DataAccessException("No such auth token");
        } else {
            var statement1 = "DELETE FROM gametable WHERE id=?";
            executeUpdate(statement1, deleteMe);
        }
    }

    @Override
    public AuthData findByToken(String authToken) throws SQLException, DataAccessException {
        var conn = DatabaseManager.getConnection();
        var statement = "SELECT * FROM authtable WHERE authtoken=?";
        var ps = conn.prepareStatement(statement);
        var rs = ps.executeQuery(authToken);
        return readToAuthDataObject(rs);
    }

    private AuthData readToAuthDataObject(ResultSet rs) {
        int gameID = 0;
        String username = null;
        String authToken = null;

        try {
            username = rs.getString("username");
            authToken = rs.getString("authtoken");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new AuthData(authToken, username);
    }

}
