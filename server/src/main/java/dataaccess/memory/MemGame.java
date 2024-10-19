package dataaccess.memory;

import chess.ChessGame;
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
    public void addUser(String username, ChessGame.TeamColor color, String gameName) {
        for (GameData game : games) {
            if (game.gameName().equals(gameName)) {
                GameData updatedGame = null;
                switch (color) {
                    case WHITE:
                        updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                        break;
                    case BLACK:
                        updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                        break;
                }
                games.remove(game);
                games.add(updatedGame);
            }
        }
    }
}
