package server;

import com.google.gson.Gson;
import dataaccess.UserDAO;
import dataaccess.memory.MemAuth;
import dataaccess.memory.MemGame;
import dataaccess.memory.MemUser;
import dataaccess.sql.sqlUser;
import model.UserData;
import service.Service;
import spark.*;
import dataaccess.*;


public class Server {
    UserDAO userDataAccess = new MemUser();
    GameDAO gameDataAccess = new MemGame();
    AuthDAO authDataAccess = new MemAuth();

    private final Service service = new Service(userDataAccess, authDataAccess, gameDataAccess);

    public Server() {
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

        Spark.exception(Exception.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object exceptionHandler(Exception ex, Request req, Response res) {

    }

    private Object join(Request request, Response response) {

        var pet = new Gson().fromJson(request.body(), String.class);
//        if (service.authenticate(ad)){

//        }
        //serialize unauthorized messaage in the body of the response
    }

    private Object createGame(Request request, Response response) {
    }

    private Object listGames(Request request, Response response) {
    }

    private Object logout(Request request, Response response) {
    }

    private Object login(Request request, Response response) {
        var  = new Gson().fromJson(req.body(), Pet.class);
    }

    private String register(Request request, Response response) {
        UserData newUser = new Gson().fromJson(request.body(), UserData.class);
        service.createUser(newUser);
    }


    Object clear(Request req, Response res) {
        service.clear();
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


