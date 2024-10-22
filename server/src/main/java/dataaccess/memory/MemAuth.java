package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MemAuth implements AuthDAO {

    final private ArrayList<AuthData> AuthList = new ArrayList<>();

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
        int index = AuthList.indexOf(ad);
        if (index == -1) throw new DataAccessException("No such auth token");
        AuthList.remove(index);
    }

    public void clearTokens() {
        AuthList.clear();
    }

    @Override
    public AuthData findByToken(String authToken){
        for (AuthData ad : AuthList) {
            if (ad.authToken().equals(authToken)) return ad;
        }
        return null;
    }
}
