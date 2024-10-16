package dataaccess.memory;

import model.UserData;

import java.util.HashMap;

public class MemUser {
    //key: Username
    //value: UserData
    final private HashMap<String, UserData> UserList = new HashMap<> ();

    void createUser(String username, String password, String email){
        UserList.put(username, new UserData(username, password, email));
    }
    void clearUsers(){
        UserList.clear();
    }
}
