package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.memory.MemGame;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class SqlGame implements GameDAO {

    public SqlGame() throws Exception {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gametable (
              `gameid` INT NOT NULL AUTO_INCREMENT,
              `gamename` varchar(256) NOT NULL UNIQUE,
              `whiteusername` varchar(256) DEFAULT NULL,
              `blackusername` varchar(256) DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameid`),
              INDEX(`gamename`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.makeStatement(createStatements);
    }

    @Override
    public void clearGames() {

        var statement = "TRUNCATE gametable";
        try {
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int value = 0;
        if (gameName == "") {
            throw new DataAccessException("Game name cannot be empty");
        }
        for (GameData game : getAllGames()) {
            if (game.gameName().equals(gameName)) {
                throw new DataAccessException("Game name already exists");
            }
        }
        var statement = "INSERT INTO gametable (gamename, json) VALUES (?, ?)";
        var json = new Gson().toJson(new ChessGame());
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, gameName);
            ps.setString(2, json);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                value = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        return value;
    }


    @Override
    public ArrayList<GameData> getAllGames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gametable";
            try (var ps = conn.prepareStatement(statement)) {
                var rs = ps.executeQuery();
                while (rs.next()) {
                    result.add(rowToGameDataObject(rs));
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

                    extractedLogic(username, color, gameID, game);

                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static void extractedLogic(String username, ChessGame.TeamColor color, int gameID, GameData game) throws DataAccessException {
        GameData updatedGame = getGameData(username, color, game);
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
            if (updatedGame.whiteUsername() != null) {
                ps2.setString(3, updatedGame.whiteUsername());
            } else {
                ps2.setNull(3, NULL);
            }
            if (updatedGame.blackUsername() != null) {
                ps2.setString(4, updatedGame.blackUsername());
            } else {
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

    private static GameData getGameData(String username, ChessGame.TeamColor color, GameData game) throws DataAccessException {
        return MemGame.extractedFromAddUser(username, color, game);
    }

    @Override
    public void updateGame(GameData updatedGameData) {
        int id = updatedGameData.gameID();
        String name = updatedGameData.gameName();
        String whiteUsername = updatedGameData.whiteUsername();
        String blackUsername = updatedGameData.blackUsername();
        var statement1 = "DELETE FROM gametable WHERE gameid=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement1)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var statement2 = "INSERT INTO gametable (gameid, gamename, whiteusername, blackusername, json) VALUES (?, ?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement2)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, whiteUsername);
            ps.setString(4, blackUsername);
            ps.setString(5, new Gson().toJson(updatedGameData));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }
}
