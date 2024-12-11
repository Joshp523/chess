package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Message;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.*;

@WebSocket
public class WebSocketFacade extends Endpoint {
    String authToken;
    Session session;
    MessageHandler messageHandler;
    int id;
    URI socketURI;
    ChessGame.TeamColor color;
    ChessClient sponsor;

    public WebSocketFacade(String url, ui.MessageHandler messageHandler, String authToken, int gameID, ChessGame.TeamColor color, ChessClient caller) throws Exception{
        this.color = color;
        this.sponsor = caller;
        id = gameID;
        try {
            url = url.replace("http", "ws");
            this.socketURI = new URI(url + "/ws");
            this.messageHandler = messageHandler;
            this.authToken = authToken;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    sponsor.printBoard(serverMessage.getChessBoard());
                    messageHandler.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception();
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        var command = new UserGameCommand(CONNECT, authToken, id, null);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void resign() throws IOException {
        var command = new UserGameCommand(RESIGN, authToken, id, null);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void leaveGame() throws IOException {
        var command = new UserGameCommand(LEAVE, authToken, id, null);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void makeMove(ChessMove move) throws IOException {
        var command = new UserGameCommand(MAKE_MOVE, authToken, id, move);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

}
