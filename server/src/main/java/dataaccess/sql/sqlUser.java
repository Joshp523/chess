package dataaccess.sql;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.sql.*;
import java.util.HashMap;

public class sqlUser implements UserDAO {
    @Override
    public void createUser(UserData ud) throws DataAccessException {

    }

    @Override
    public void clearUsers() {

    }

    @Override
    public UserData findByUnPwd(String username, String password) {
        return null;
    }

    @Override
    public UserData findByUsername(String username) {
        return null;
    }

    @Override
    public HashMap<String, UserData> getUserList() {
        return null;
    }
}
