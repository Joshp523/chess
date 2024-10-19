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

    public void clear(){
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearTokens();
    }

    public boolean authenticate(AuthData ad) throws DataAccessException {
        return authDAO.authenticate(ad);
    }

    public String createUser(UserData user) throws DataAccessException {
        userDAO.createUser(user);
        return authDAO.createAuthToken(user);
    }
}
