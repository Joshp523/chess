package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        userDAO.createUser(user);
        return login(user);
    }

    public AuthData login(UserData user) throws DataAccessException {

    return null;
    }

    public void logout(UserData user){

    }

    public void clearUsers(){
        userDAO.clearUsers();
    }
}
