package dataaccess.memory;

import chess.ChessGame;
import model.GameData;
import model.UserData;


import java.util.HashMap;

public class MemGame {
    //key: Username
    //value: UserData
    final private HashMap<String, GameData> UserList = new HashMap<> ();

    ChessGame getGame(String gameName){}
    int createGame(String gameName){}
    void deleteGame(String gameName){}
    void clearGames(){}
    void updateGame(ChessGame.TeamColor color, ChessGame game, String username){}

}
