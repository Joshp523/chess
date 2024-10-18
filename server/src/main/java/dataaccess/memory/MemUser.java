package dataaccess.memory;

import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemUser implements UserDAO {
    //key: username
    //value: UserData
    final private HashMap<UserData, AuthData> UserList = new HashMap<> ();

     public void createUser(UserData u){
        UserList.put(u, null);
    }
    public boolean validateUser(AuthData ad){
        return UserList.containsValue(ad);
    }
    public void clearUsers(){
        UserList.clear();
    }
}
