package dataaccess.memory;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;

import static chess.ChessGame.TeamColor.WHITE;

public class MemGame implements GameDAO {
    static int id = 0;
    ArrayList<GameData> games = new ArrayList<>();

    @Override
    public int createGame(String gameName) throws DataAccessException {
        //System.out.println("gameName: " + gameName);
        id += 1;
        GameData newGame = new GameData(id, null, null, gameName, new ChessGame());
        games.add(newGame);
        return id;
    }

    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return games;
    }

    @Override
    public void addUser(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                GameData updatedGame = null;
                switch (color) {
                    case WHITE:
                        if (game.whiteUsername() == null) {
                            updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                        } else throw new DataAccessException("Error: already taken");
                        break;
                    case BLACK:
                        if (game.blackUsername() == null) {
                            updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                        } else throw new DataAccessException("Error: already taken");
                        break;
                }
                games.remove(game);
                games.add(updatedGame);
            }
        }
    }
}
