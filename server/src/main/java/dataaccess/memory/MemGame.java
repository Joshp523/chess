package dataaccess.memory;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

import static chess.ChessGame.TeamColor.WHITE;

public class MemGame {
    static int gameID = 0;
    //key: gameName
    //value: GameData
    final private HashMap<String, GameData> UserList = new HashMap<>();

    ChessGame getGame(String gameName) {
        return UserList.get(gameName).game();
    }

    int createGame(String gameName) {
        gameID += 1;
        UserList.put(gameName, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    void deleteGame(String gameName) {
        UserList.remove(gameName);
    }

    void addPlayer(String gameName, String username, ChessGame.TeamColor color) {
        GameData OldGameData = UserList.get(gameName);
        GameData NewGameData = OldGameData ;

        int oldGameID = OldGameData.gameID();
        String oldWhiteUsername = OldGameData.whiteUsername();
        String oldBlackUsername = OldGameData.blackUsername();
        String oldGameName = OldGameData.gameName();
        ChessGame oldGame = OldGameData.game();

        switch (color) {
            case WHITE:
                NewGameData = new GameData(oldGameID, username, oldBlackUsername, oldGameName, oldGame);
                break;
            case BLACK:
                NewGameData = new GameData(oldGameID, oldWhiteUsername, username, oldGameName, oldGame);
                break;
        }

        UserList.replace(gameName, NewGameData);
    }

    void updateGame(String gameName, ChessGame newGame) {
        GameData OldGameData = UserList.get(gameName);

        int oldGameID = OldGameData.gameID();
        String oldWhiteUsername = OldGameData.whiteUsername();
        String oldBlackUsername = OldGameData.blackUsername();
        String oldGameName = OldGameData.gameName();
        ChessGame oldGame = OldGameData.game();

        GameData newGameData = new GameData(oldGameID, oldWhiteUsername, oldBlackUsername, oldGameName, newGame);

        UserList.replace(gameName, newGameData);
    }

    void clearGames() {
        UserList.clear();
    }

}
