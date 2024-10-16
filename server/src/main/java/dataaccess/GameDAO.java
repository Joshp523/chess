package dataaccess;

import chess.ChessGame;

public interface GameDAO {
    ChessGame getGame(String gameName);
    int createGame(String gameName);
    void deleteGame(String gameName);
    void clearGames();
    void updateGame(ChessGame.TeamColor color, ChessGame game, String username);
}
