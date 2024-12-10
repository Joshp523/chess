package ui;


import server.Message;

public interface MessageHandler {
    void notify(Message message);
}
