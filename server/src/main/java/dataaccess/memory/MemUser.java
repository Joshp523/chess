package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemUser implements UserDAO {
    //key: UserData.username
    //value: UserData
    final private HashMap<String, UserData> UserList = new HashMap<>();

    public void createUser(UserData u)throws DataAccessException {
        if(UserList.get(u.username())!=null){
            throw new DataAccessException("Error: already taken");
        }
        UserList.put(u.username(), u);
    }

    public void clearUsers() {
        UserList.clear();
    }

    @Override
    public UserData findByUnPwd(String username, String password) throws DataAccessException{
        UserData checkMe = UserList.get(username);
        if (checkMe != null && checkMe.password().equals(password)) {
            return checkMe;
        }throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public UserData findByUsername(String username) {
        return UserList.get(username);
    }

    public HashMap<String, UserData> getUserList() {
        return UserList;
    }
}
