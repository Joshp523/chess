package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    int createGame(String gameName) throws DataAccessException;

    void clearGames();

    ArrayList<GameData> getAllGames();

    void addUser(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException;

    void updateGame(GameData updatedGameData);
}
