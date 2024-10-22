package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class SqlAuth implements AuthDAO {

    @Override
    public String createAuthToken(UserData ud) throws DataAccessException {
        return "";
    }


    @Override
    public void deleteAuthToken(AuthData ad) throws DataAccessException {
    }

    @Override
    public void clearTokens() {

    }

    @Override
    public AuthData findByToken(String authToken) {
        return null;
    }

}
