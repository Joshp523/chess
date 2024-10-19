package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;

public class Service {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public Service(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
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

    public void clear(){
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearTokens();
    }
}
