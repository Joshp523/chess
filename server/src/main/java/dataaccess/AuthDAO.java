package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    boolean findUser(UserData ud);
    int createAuthToken(UserData ud) throws DataAccessException;
    void deleteAuthToken(AuthData ad) throws DataAccessException;
    String fetchUsername(AuthData ad) throws DataAccessException;
    void clearTokens();
}
