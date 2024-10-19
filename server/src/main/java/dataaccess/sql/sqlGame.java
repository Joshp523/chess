package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;

public class sqlGame implements GameDAO {

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
}
