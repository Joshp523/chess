package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemUser implements UserDAO {
    //key: UserData.username
    //value: UserData
    final private HashMap<String, UserData> UserList = new HashMap<>();

    public void createUser(UserData u) {
        UserList.put(u.username(), u);
    }

    public void clearUsers() {
        UserList.clear();
    }

    @Override
    public UserData findByUnPwd(String username, String password) {
        UserData checkMe = UserList.get(username);
        if (checkMe.password().equals(password)) {
            return checkMe;
        }return null;
    }
}
