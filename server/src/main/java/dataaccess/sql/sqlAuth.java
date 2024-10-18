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
    public int createAuthToken(UserData ud) throws DataAccessException {
        return 0;
    }

    @Override
    public void deleteAuthToken(AuthData ad) throws DataAccessException {

    }

    @Override
    public String fetchUsername(AuthData ad) throws DataAccessException {
        return "";
    }

    @Override
    public void clearTokens() {

    }
}
