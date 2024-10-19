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
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::join);


//        Spark.post("/user", () -> );
//        Spark.exception(Exception.class, () -> this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object join(Request request, Response response) {
    }

    private Object createGame(Request request, Response response) {
    }

    private Object listGames(Request request, Response response) {
    }

    private Object logout(Request request, Response response) {
    }

    private Object login(Request request, Response response) {
    }

    private Object register(Request request, Response response) {
    }


    Object clear(Request req, Response res) {
        Service.clear();
        res.status(200);
        return "";
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}


