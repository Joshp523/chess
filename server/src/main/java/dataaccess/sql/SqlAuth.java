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
    public void clearTokens() {
        var statement = "TRUNCATE authtable";
        try {
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createAuthToken(UserData ud) throws DataAccessException {
        if (ud.username() == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        try {
            var statement = "INSERT INTO authtable (username, authtoken) VALUES (?, ?)";
            String newToken = UUID.randomUUID().toString();
            try(var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement)){
                ps.setString(2, newToken);
                ps.setString(1, ud.username());
                ps.executeUpdate();
            }
            return newToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteAuthToken(AuthData ad) throws DataAccessException {
        var statement1 = "DELETE FROM authtable WHERE authtoken=?";
        try (
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement1)){
            ps.setString(1, ad.username());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public AuthData findByToken(String authToken) {
        try {
            var conn = DatabaseManager.getConnection();
            var statement = "SELECT * FROM authtable WHERE authtoken=?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);
            var rs = ps.executeQuery();
            System.out.print(ps);
            System.out.print("authToken: " + authToken);
            return readToAuthDataObject(rs);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthData readToAuthDataObject(ResultSet rs) {
        int gameID = 0;
        String username = null;
        String authToken = null;
        try {
            if (rs.next()) {
                username = rs.getString("username");
                authToken = rs.getString("authtoken");
            } else {
                throw new DataAccessException("the table is empty");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new AuthData(authToken, username);
    }
}
