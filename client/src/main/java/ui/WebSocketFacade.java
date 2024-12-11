package ui;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Message;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import ui.MessageHandler;
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

    public WebSocketFacade(String url, ui.MessageHandler messageHandler, String authToken, int gameID) throws Exception{
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
                    System.out.println(message);
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


    public void resign(String authToken, int gameID) throws IOException {
        var command = new UserGameCommand(RESIGN, authToken, gameID, null);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        var command = new UserGameCommand(LEAVE, authToken, gameID, null);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }


}
