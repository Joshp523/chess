import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlGame;
import dataaccess.sql.SqlUser;
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
            /** Comment out one of the following two lines, depending on the data storage option you'd prefer. **/
            var service = new Service(new SqlUser(), new SqlAuth(), new SqlGame());
//            var service = new Service(new MemUser(), new MemAuth(), new MemGame());
            var server = new Server().run(port);
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }



}