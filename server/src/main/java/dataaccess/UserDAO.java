package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;

public interface UserDAO {

    void createUser(UserData ud) throws DataAccessException;
    void clearUsers();
    UserData findByUnPwd(String username, String password)throws DataAccessException;

    UserData findByUsername(String username);

    HashMap<String, UserData> getUserList();
    //boolean validateUser(AuthData ad) throws DataAccessException;
}
