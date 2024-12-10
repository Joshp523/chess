package ui;

import chess.ChessPosition;
import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Message;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import javax.management.Notification;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.LEAVE;
import static websocket.commands.UserGameCommand.CommandType.RESIGN;

@WebSocket
public class WebSocketFacade extends Endpoint {
    String authToken;
    Session session;
    MessageHandler messageHandler;

    public WebSocketFacade(String url, MessageHandler messageHandler) throws Exception{
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageHandler = messageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            //set message handler
            this.session.addMessageHandler(new javax.websocket.MessageHandler() {
                public void onMessage(String message){
                    Message msg = new Gson().fromJson(message, Message.class);
                    messageHandler.notify(msg);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception();
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) { this.session = session; }


    public void resign(String authToken, int gameID) throws IOException {
        var command = new UserGameCommand(RESIGN, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        var command = new UserGameCommand(LEAVE, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }


}
