import dataaccess.UserDAO;
import dataaccess.memory.MemUser;
import dataaccess.sql.sqlUser;
import server.Server;
import service.UserService;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            UserDAO userDataAccess = new MemUser();
            if (args.length >= 2 && args[1].equals("dataaccess/sql")) {
                userDataAccess = new sqlUser();
            }

            var service = new UserService(userDataAccess);
            var server = new Server(service).run(port);
            //port = server.port;
            System.out.printf("Server started on port %d with %s%n", port, userDataAccess.getClass());
            return;
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }



}