package service;

import chess.ChessBoard;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.UserDAO;

import java.sql.SQLException;
import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

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
        try {
            AuthData authData = authDAO.findByToken(authToken);
            authDAO.deleteAuthToken(authData);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean validToken(String authToken) {
        try {
            return authDAO.findByToken(authToken) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<GameData> listGames() {
        return gameDAO.getAllGames();
    }

    public int createGame(String gameName) throws DataAccessException {
        //System.out.println("got to service class");
        return gameDAO.createGame(gameName);
    }

    public UserData findUserByToken(String authToken) throws DataAccessException {
        try {
            AuthData authData = authDAO.findByToken(authToken);
            String username = authData.username();
            return userDAO.findByUsername(username);
        } catch (SQLException e) {
            throw new DataAccessException("unauthorized");
        }
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException, SQLException {
        UserData userData = findUserByToken(authToken);
        String username = userData.username();
        gameDAO.addUser(username, color, gameID);
    }

    public GameData getGameByID(int id){
        for (GameData gameData : gameDAO.getAllGames()) {
            if (gameData.gameID() == id){
                return gameData;
            }
        }
        return null;
    }

    public void updateGame(int gameID, ChessGame newGame)throws DataAccessException{
        GameData old = getGameByID(gameID);
        GameData updatedGameData = new GameData(gameID, old.whiteUsername(), old.blackUsername(), old.gameName(), newGame);
        gameDAO.updateGame(updatedGameData);
    }

    public void leaveGame(String authToken, int gameID) throws DataAccessException {
        UserData userData = findUserByToken(authToken);
        String username = userData.username();
        GameData game = getGameByID(gameID);
        ChessGame.TeamColor color = null;
        if (game.whiteUsername()==username){color = WHITE;}
        else if (game.blackUsername() == username){color = BLACK;}
        gameDAO.addUser(null, color, gameID);
    }
}
