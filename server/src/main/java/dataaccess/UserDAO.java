package dataaccess;

import org.eclipse.jetty.server.Authentication;

public interface UserDAO {

    void createUser(String username, String password);
    void clearUsers();
}
