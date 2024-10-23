package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.UUID;

public class MemAuth implements AuthDAO {

    final private ArrayList<AuthData> authList = new ArrayList<>();

    @Override
    public String createAuthToken(UserData ud) throws DataAccessException {
        if (ud.username()==null) {
            throw new DataAccessException("Error: unauthorized");
        }
        String newToken = UUID.randomUUID().toString();
        authList.add(new AuthData(newToken, ud.username()));
        return newToken;
    }

    @Override
    public void deleteAuthToken(AuthData ad) throws DataAccessException {
        int index = authList.indexOf(ad);
        if (index == -1) {
            throw new DataAccessException("No such auth token");
        }
        authList.remove(index);
    }

    public void clearTokens() {
        authList.clear();
    }

    @Override
    public AuthData findByToken(String authToken){
        for (AuthData ad : authList) {
            if (ad.authToken().equals(authToken)) {
                return ad;
            }
        }
        return null;
    }
}
