import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server newServer = new Server();
        newServer.run(8080);
    }
}