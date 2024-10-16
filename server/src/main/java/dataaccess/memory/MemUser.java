package dataaccess.memory;

import model.UserData;

import java.util.HashMap;

public class MemUser {
    //key: username
    //value: UserData
    final private HashMap<String, UserData> UserList = new HashMap<> ();

    void createUser(UserData u){
        UserList.put(u.username, u);
    }
    void clearUsers(){
        UserList.clear();
    }
}
