package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
        DatabaseManager.makeStatement(createStatements);
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
            ps.setString(1, ad.authToken());
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
