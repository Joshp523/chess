package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlGame;
import dataaccess.sql.SqlUser;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Message;
import service.Service;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    Service service = new Service(new SqlUser(), new SqlAuth(), new SqlGame());

    public WebSocketHandler() throws Exception {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
            case LEAVE -> leave(command.getAuthToken(), command.getGameID(), session);
            case RESIGN -> resign(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void resign(String authToken, Integer gameID, Session session) throws IOException, SQLException, DataAccessException {
        connections.add(authToken, gameID, session);
        String loser = service.findUserByToken(authToken).username();
        String winner;
        if(service.getGameByID(gameID).whiteUsername()!=loser) {
            winner = service.getGameByID(gameID).whiteUsername();
        }else{
            winner = service.getGameByID(gameID).blackUsername();
        }
        var message = String.format("%s has forfeited the game. %s wins!", loser, winner);
        var notification = new Message(message);
        connections.broadcast(authToken, notification);
    }

    private void connect(String authToken, int gameID, Session session) throws IOException, SQLException, DataAccessException {
        connections.add(authToken, gameID, session);
        var message = String.format(" %s has joined the game as ", service.findUserByToken(authToken).username());
        var notification = new Message(message);
        connections.broadcast(authToken, notification);
    }

    private void makeMove(String authToken, int gameID, ChessMove move) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        ChessGame game = getGameFromID(gameID);
        game.makeMove(move);
        service.updateGame(gameID, game);
        var message = String.format("%s moved.", service.findUserByToken(authToken).username());
        var notification = new Message(message);
        connections.broadcast(authToken, notification);
    }

    public void leave(String authToken, int gameID, Session session) throws Exception {
        try {
            connections.remove(authToken);
            service.leaveGame(authToken, gameID);
            var message = String.format("%s has left the game", service.findUserByToken(authToken).username());
            var notification = new Message(message);
            connections.broadcast(authToken, notification);
        } catch (Exception ex) {
            throw new Exception();
        }
    }

    private ChessGame getGameFromID(int id){
        for (GameData data: service.listGames()){
            if (data.gameID()==id){
                return data.game();
            }
        }
        return null;
    }
}
