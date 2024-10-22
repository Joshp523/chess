package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;

public class SqlGame implements GameDAO {

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }


    @Override
    public void clearGames() {

    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return null;
    }

    @Override
    public void addUser(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException{

    }
}
