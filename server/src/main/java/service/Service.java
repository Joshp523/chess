package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;

import java.util.ArrayList;

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

    public void createUser(UserData user) throws DataAccessException {
        userDAO.createUser(user);
    }

    public String createAuthToken(UserData ud) throws DataAccessException {
        return authDAO.createAuthToken(ud);
    }

    public UserData findDataByUnPwd(String username, String password) {
        return userDAO.findByUnPwd(username, password);
    }

    public boolean logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.findByToken(authToken);
        return authDAO.deleteAuthToken(authData);
    }

    public boolean validToken(String authToken) {
        if(authDAO.findByToken(authToken) != null)return true;
        return false;
    }

    public ArrayList listGames() {
        return gameDAO.getAllGames();
    }

    public int createGame(String gameName) throws DataAccessException {
        return gameDAO.createGame(gameName);
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, String gameName) throws DataAccessException {
        UserData userData = authDAO.findUserByToken(authToken);
        String username = userData.username();
        gameDAO.addUser(username, color, gameName);
    }
}
