package dataaccess.sql;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.sql.*;

public class sqlUser implements UserDAO {
    @Override
    public void createUser(UserData ud) throws DataAccessException {

    }

    @Override
    public void clearUsers() {

    }

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        return null;
    }
}
