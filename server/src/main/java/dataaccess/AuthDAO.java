package dataaccess;

public interface AuthDAO {
    boolean findUser(String username);
    int createAuthToken(String username) throws DataAccessException;
    void deleteAuthToken(int authToken) throws DataAccessException;
    String fetchUsername(int authToken) throws DataAccessException;
    void clearTokens();
}
