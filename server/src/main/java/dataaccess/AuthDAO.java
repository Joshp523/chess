package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public interface AuthDAO {
    String createAuthToken(UserData ud) throws DataAccessException;
    void deleteAuthToken(AuthData ad) throws DataAccessException;
    void clearTokens();
    AuthData findByToken(String authToken) throws SQLException, DataAccessException;
}
