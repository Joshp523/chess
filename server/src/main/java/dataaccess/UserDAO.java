package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface UserDAO {

    void createUser(UserData ud) throws DataAccessException;
    void clearUsers();
    boolean validateUser(AuthData ad) throws DataAccessException;
}
