package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MemAuth implements AuthDAO {

    final private ArrayList<AuthData> AuthList = new ArrayList<> ();

    @Override
    public boolean findUser(UserData ud) {
        return false;
    }

    @Override
    public String createAuthToken(UserData ud) throws DataAccessException {
        String newToken = UUID.randomUUID().toString();
        AuthList.add(new AuthData(newToken, ud.username()));
        return newToken;
    }

    @Override
    public void deleteAuthToken(AuthData ad) throws DataAccessException {

    }

    @Override
    public boolean authenticate(AuthData ad) throws DataAccessException {
        return AuthList.contains(ad);
    }

    @Override
    public String fetchUsername(AuthData ad) throws DataAccessException {
        return "";
    }

    public void clearTokens(){
        AuthList.clear ();
    }

    public boolean validateUser(AuthData ad){
        return AuthList.contains(ad);
    }
}
