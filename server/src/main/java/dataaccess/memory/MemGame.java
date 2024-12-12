package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;

public class MemGame implements GameDAO {
    static int id = 0;
    ArrayList<GameData> games = new ArrayList<>();

    @Override
    public int createGame(String gameName) throws DataAccessException {
        if(gameName == "") {
            throw new DataAccessException("Game name cannot be empty");
        }
        for(GameData game: games){
            if (game.gameName()==gameName){
                throw new DataAccessException("Game name already exists");}
        }
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
                GameData updatedGame = extractedFromAddUser(username, color, game);
                games.remove(game);
                games.add(updatedGame);
            }
        }
    }

    @Override
    public void updateGame(GameData updatedGameData) {    }

    public static GameData extractedFromAddUser(String username, ChessGame.TeamColor color, GameData game) throws DataAccessException {
        if(color!=null){
        switch (color) {
            case WHITE:
                if (game.whiteUsername() != null&&username != null) {
                    throw new DataAccessException("Error: already taken");
                } else {
                    return new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                }
            case BLACK:
                if (game.blackUsername() != null&&username!=null) {
                    throw new DataAccessException("Error: already taken");
                } else {
                    return new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                }
        }}
        return new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    }
}
