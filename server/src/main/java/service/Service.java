package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
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

    public void clear() {
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

    public UserData findDataByUnPwd(String username, String password) throws DataAccessException {
        return userDAO.findByUnPwd(username, password);
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.findByToken(authToken);
        authDAO.deleteAuthToken(authData);
    }

    public boolean validToken(String authToken) {
        return authDAO.findByToken(authToken) != null;

    }

    public ArrayList<GameData> listGames() {
        return gameDAO.getAllGames();
    }

    public int createGame(String gameName) throws DataAccessException {
        //System.out.println("got to service class");
        return gameDAO.createGame(gameName);
    }

    private UserData findUserByToken(String authToken) throws DataAccessException {
        AuthData authData = authDAO.findByToken(authToken);
        String username = authData.username();
        return userDAO.findByUsername(username);
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        UserData userData = findUserByToken(authToken);
        String username = userData.username();
        gameDAO.addUser(username, color, gameID);
    }
}
