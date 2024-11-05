package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
              `whiteusername` varchar(256) NOT NULL,
              `blackusername` varchar(256) NOT NULl,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameid`),
              INDEX(`gamename`),
              INDEX(`whiteusername`),
              INDEX(`blackusername`)
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
        } catch (SQLException ex) {
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
                        //else if (param instanceof  p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public void clearGames() {
        var statement = "TRUNCATE gametable";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        if(gameName == "") {
            throw new DataAccessException("Game name cannot be empty");
        }
        for(GameData game: getAllGames()){
            if (game.gameName().equals(gameName)){
                throw new DataAccessException("Game name already exists");}
        }
        id += 1;
        var statement = "INSERT INTO gametable (gameid, gamename, whiteusername, blackusername, json) VALUES (?, ?, ?, ?, ?)";
        var json = new Gson().toJson(new ChessGame());
        executeUpdate(statement,id, gameName, null, null, json);
        return id;
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
//            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        var game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public void addUser(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException {

    }
}
