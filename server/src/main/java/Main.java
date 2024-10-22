import dataaccess.memory.MemAuth;
import dataaccess.memory.MemGame;
import dataaccess.memory.MemUser;
import server.Server;
import service.Service;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }
            var service = new Service(new MemUser(), new MemAuth(), new MemGame());
            var server = new Server().run(port);
//            port = server.port();
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }



}