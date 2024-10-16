package dataaccess;

import chess.ChessGame;

public interface GameDAO {
    ChessGame getGame(String gameName);
    int createGame(String gameName) throws DataAccessException;
    void deleteGame(String gameName) throws DataAccessException;
    void updateGame(String gameName, ChessGame game) throws DataAccessException;
    void addPlayer(String gameName, String username, ChessGame.TeamColor color) throws DataAccessException;
    void clearGames();
}
