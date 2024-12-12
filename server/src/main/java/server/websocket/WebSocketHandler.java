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
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

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
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getMove(), session);
            case LEAVE -> leave(command.getAuthToken(), command.getGameID(), session);
            case RESIGN -> resign(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void resign(String authToken, Integer gameID, Session session) throws IOException, SQLException, DataAccessException {
        if (service.getGameByID(gameID).game().gameOver() != null) {
            var error = new ServerMessage(ERROR, null,  null,"over");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        connections.add(authToken, gameID, session);
        String loser = service.findUserByToken(authToken).username();
        String winner;
        if (!service.getGameByID(gameID).whiteUsername().equals(loser)) {
            winner = service.getGameByID(gameID).whiteUsername();
        } else {
            winner = service.getGameByID(gameID).blackUsername();
        }
        var message = String.format("%s has forfeited the game. %s wins!", loser, winner);
        var notification = new ServerMessage(NOTIFICATION, null, message, null);
        connections.broadcast(null, notification);
        ChessGame updatedGame = service.getGameByID(gameID).game();
        updatedGame.resign();
        service.updateGame(gameID, updatedGame);
    }

    private void connect(String authToken, Integer gameID, Session session) throws IOException, SQLException, DataAccessException {
        if (gameID == null) {
            var error = new ServerMessage(ERROR, null, null, "you need a gameID for this to work.");
            session.getRemote().sendString(new Gson().toJson(error));
        }
        if (authToken == null) {
            var error = new ServerMessage(ERROR, null, null, "you need a gameID for this to work.");
            session.getRemote().sendString(new Gson().toJson(error));
        } else if (service.getGameByID(gameID) == null) {
            var error = new ServerMessage(ERROR, null, null, "invalid gameID");
            session.getRemote().sendString(new Gson().toJson(error));
        } else {
            try {
                connections.add(authToken, gameID, session);
                String player = service.findUserByToken(authToken).username();
                String role;
                if (service.getGameByID(gameID).whiteUsername() == player) {
                    role = "white";
                } else if (service.getGameByID(gameID).blackUsername() == player) {
                    role = "black";
                } else {
                    role = "an observer";
                }
                var message = String.format(" %s has joined the game as %s", player, role);
                var notification = new ServerMessage(NOTIFICATION, null, message, null);
                connections.broadcast(authToken, notification);
                Connection c = this.connections.connections.get(authToken);
                ServerMessage response = new ServerMessage(LOAD_GAME, service.getGameByID(gameID).game().getBoard(), null, null);
                c.send(new Gson().toJson(response));

            } catch (Exception e) {
                var error = new ServerMessage(ERROR, null, null, "You are not authorized to perform this action.");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        }

    }

    private void makeMove(String authToken, int gameID, ChessMove move, Session session) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        if (service.getGameByID(gameID).game().gameOver() != null) {
            ServerMessage response = new ServerMessage(ERROR, null, null, service.getGameByID(gameID).game().gameOver());
            session.getRemote().sendString(new Gson().toJson(response));
            return;
        }
        try {
            ChessGame.TeamColor pieceColor = service.getGameByID(gameID).game().getBoard().getPiece(move.getStartPosition()).getTeamColor();
            String playerUsername = service.findUserByToken(authToken).username();
            ChessGame.TeamColor playerColor = ChessGame.TeamColor.GREEN;
            if (service.getGameByID(gameID).whiteUsername().equals(playerUsername)) {
                playerColor = ChessGame.TeamColor.WHITE;
            }
            if (service.getGameByID(gameID).blackUsername().equals(playerUsername)) {
                playerColor = ChessGame.TeamColor.BLACK;
            }
            if (!pieceColor.equals(playerColor)) {
                var error = new ServerMessage(ERROR, null, null, "You can only move your own pieces.");
                session.getRemote().sendString(new Gson().toJson(error));
            } else {
                ChessGame game = getGameFromID(gameID);
                try {
                    assert game != null;
                    game.makeMove(move);
                } catch (InvalidMoveException e) {
                    var error = new ServerMessage(ERROR, null, null, "invalid move");
                    session.getRemote().sendString(new Gson().toJson(error));
                    return;
                }
                service.updateGame(gameID, game);
                var message = String.format("%s moved.", service.findUserByToken(authToken).username());
                var notification = new ServerMessage(NOTIFICATION, null, message, null);
                connections.broadcast(authToken, notification);
                Connection c = this.connections.connections.get(authToken);
                ServerMessage response = new ServerMessage(LOAD_GAME, service.getGameByID(gameID).game().getBoard(), null, null);
                connections.broadcast(null, response);
            }
        } catch (Exception e) {
            var error = new ServerMessage(ERROR, null, null, "you are not authorized to perform this action.");
            session.getRemote().sendString(new Gson().toJson(error));
        }
        if (service.getGameByID(gameID).game().gameOver() != null) {
            ServerMessage response = new ServerMessage(NOTIFICATION, null, service.getGameByID(gameID).game().gameOver(), null);
            connections.broadcast(null, response);
        }

    }

    public void leave(String authToken, int gameID, Session session) throws Exception {
        try {
            connections.remove(authToken);
            service.leaveGame(authToken, gameID);
            var message = String.format("%s has left the game", service.findUserByToken(authToken).username());
            var notification = new ServerMessage(NOTIFICATION, null, message, null);
            connections.broadcast(authToken, notification);
            connections.remove(authToken);
        } catch (Exception ex) {
            var error = new ServerMessage(ERROR, null, null, "error");
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }

    private ChessGame getGameFromID(int id) {
        for (GameData data : service.listGames()) {
            if (data.gameID() == id) {
                return data.game();
            }
        }
        return null;
    }
}
