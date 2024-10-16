package dataaccess.memory;

import java.util.HashMap;

public class MemAuth {
    private static int nextID = 0;
    //key: AuthToken
    //value: username
    final private HashMap<Integer, String> AuthList = new HashMap<> ();


    boolean findUser(String username){
        return AuthList.containsValue(username);
    }
    int createAuthToken(String username){
        nextID++;
        AuthList.put(nextID, username);
        return nextID;
    }
    void deleteAuthToken(int authToken){
        AuthList.remove(authToken);
    }
    String fetchUsername(int authToken){
        return AuthList.get(authToken);
    }
    void clearTokens(){
        AuthList.clear ();
    }
}
