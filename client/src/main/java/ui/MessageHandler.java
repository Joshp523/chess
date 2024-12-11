package ui;


import server.Message;
import websocket.messages.ServerMessage;

public interface MessageHandler {
    void notify(ServerMessage message);
}
