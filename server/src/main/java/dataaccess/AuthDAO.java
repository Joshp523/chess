package dataaccess;

public interface AuthDAO {
    boolean findUser(String username);
    int createAuthToken(String username);
    void deleteAuthToken(int authToken);
    String fetchUsername(int authToken);
    void clearTokens();
}
