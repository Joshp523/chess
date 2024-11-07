package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.ResultSet;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlGame implements GameDAO {
    static int id = 0;

    public SqlGame() throws Exception {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gametable (
              `gameid` int,
              `gamename` varchar(256) NOT NULL,
              `whiteusername` varchar(256) DEFAULT NULL,
              `blackusername` varchar(256) DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameid`),
              INDEX(`gamename`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public void clearGames() {
        var statement = "TRUNCATE gametable";
        try {
            executeUpdate(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {

        if (gameName == "") {
            throw new DataAccessException("Game name cannot be empty");
        }
        for (GameData game : getAllGames()) {
            if (game.gameName().equals(gameName)) {
                throw new DataAccessException("Game name already exists");
            }
        }
        id += 1;
        var statement = "INSERT INTO gametable (gameid, gamename, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(new ChessGame());

        try (var conn = DatabaseManager.getConnection();

             var ps = conn.prepareStatement(statement)) {
            ps.setInt(1, id);
            ps.setString(2, gameName);
            ps.setString(3, json);
            ps.executeUpdate();
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ArrayList<GameData> getAllGames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gametable";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(rowToGameDataObject(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private GameData rowToGameDataObject(ResultSet rs) {
        int gameID = 0;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = null;
        String json = null;
        try {
            gameID = rs.getInt("gameid");
            whiteUsername = rs.getString("whiteusername");
            blackUsername = rs.getString("blackusername");
            gameName = rs.getString("gamename");
            json = rs.getString("json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        var game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public void addUser(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        try {
            for (GameData game : getAllGames()) {
                if (game.gameID() == gameID) {
                    GameData updatedGame = null;
                    switch (color) {
                        case WHITE:
                            if (game.whiteUsername()==null) {
                                updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                            } else {
                                throw new DataAccessException("Error: already taken");
                            }
                            break;
                        case BLACK:
                            if (game.blackUsername()==null) {
                                updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                            } else {
                                throw new DataAccessException("Error: already taken");
                            }
                            break;
                    }
                    var statement1 = "DELETE FROM gametable WHERE gameid=?";
                    try (var conn = DatabaseManager.getConnection();
                         var ps = conn.prepareStatement(statement1)) {
                        ps.setInt(1, gameID);
                        ps.executeUpdate();
                    } catch (Exception e) {
                        throw new DataAccessException(String.format("unable to update database: %s", e.getMessage()));
                    }
                    var statement2 = "INSERT INTO gametable (gameid, gamename, whiteusername, blackusername, json) VALUES (?, ?, ?, ?, ?)";
                    var json = new Gson().toJson(updatedGame);
                    try (var conn2 = DatabaseManager.getConnection();
                         var ps2 = conn2.prepareStatement(statement2)) {
                        if(updatedGame.whiteUsername() != null){
                            ps2.setString(3, updatedGame.whiteUsername());
                        }else{
                            ps2.setNull(3, NULL);
                        }
                        if(updatedGame.blackUsername() != null){
                            ps2.setString(4, updatedGame.blackUsername());
                        }else{
                            ps2.setNull(4, NULL);
                        }
                        ps2.setInt(1, gameID);
                        ps2.setString(2, updatedGame.gameName());

                        ps2.setString(5, json);
                        ps2.executeUpdate();
                    } catch (Exception e) {
                        throw new DataAccessException(String.format("unable to update database: %s", e.getMessage()));
                    }

                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
