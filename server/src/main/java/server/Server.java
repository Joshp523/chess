package server;

import dataaccess.UserDAO;
import service.UserService;
import spark.*;

public class Server {
    private final UserService userService;

    public Server(UserService userService) {
        this.userService = userService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.delete("/db", this::clear);

//        Spark.post("/user", () -> );
//        Spark.exception(Exception.class, () -> this::exceptionHandler);


        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
//    void createuser(Request req, Response res) throws Exception{
//        var newUser = serializer.fronJson(req.body(), )
//
//    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    Object clear(Request req, Response res) {
        userService.clearUsers();
        res.status(200);
        return "";
    }

    public int port() {
        return Spark.port();
    }
}


