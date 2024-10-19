package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class sqlAuth implements AuthDAO {
    @Override
    public boolean findUser(UserData ud) {
        return false;
    }

    @Override
    public String createAuthToken(UserData ud) throws DataAccessException {
        return "";
    }


    @Override
    public boolean deleteAuthToken(AuthData ad) throws DataAccessException {
        return false;
    }

    @Override
    public void clearTokens() {

    }

    @Override
    public AuthData findByToken(String authToken) {
        return null;
    }

}
