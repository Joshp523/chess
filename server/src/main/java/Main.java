import chess.*;
import server.Server;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        Server newServer = new Server();
        newServer.run(parseInt(args[0]));
    }
}