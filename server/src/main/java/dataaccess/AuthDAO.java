package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    boolean findUser(UserData ud);
    String createAuthToken(UserData ud) throws DataAccessException;
    void deleteAuthToken(AuthData ad) throws DataAccessException;
    void clearTokens();
    AuthData findByToken(String authToken);
}
