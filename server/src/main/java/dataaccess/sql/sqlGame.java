package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

public class sqlGame implements GameDAO {
    @Override
    public ChessGame getGame(String gameName) {
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public void deleteGame(String gameName) throws DataAccessException {

    }

    @Override
    public void updateGame(String gameName, ChessGame game) throws DataAccessException {

    }

    @Override
    public void addPlayer(String gameName, String username, ChessGame.TeamColor color) throws DataAccessException {

    }

    @Override
    public void clearGames() {

    }
}
