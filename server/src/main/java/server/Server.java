package server;

import com.google.gson.Gson;
import dataaccess.UserDAO;
import dataaccess.memory.MemAuth;
import dataaccess.memory.MemGame;
import dataaccess.memory.MemUser;
import dataaccess.sql.sqlUser;
import model.AuthData;
import model.UserData;
import service.Service;
import spark.*;
import dataaccess.*;

import java.util.Map;


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

//        Spark.exception(Exception.class, this::exceptionHandler);
//
        Spark.awaitInitialization();
        return Spark.port();
    }

//    private Object exceptionHandler(Exception ex, Request req, Response res) {
//
//    }

    private String join(Request request, Response response) {
        String authToken = request.session().attribute("authorization");
        if (service.validToken(authToken)) {
            try {
                ColorAndGame cag = new Gson().fromJson(request.body(), ColorAndGame.class);
                service.joinGame(authToken, cag.color(), cag.name());
                response.status(200);
                return "";
            } catch (DataAccessException dae) {
                response.status(400);
                return new Gson().toJson(new Message("Error: bad request"));
            }
        } else {
            response.status(401);
            Message message = new Message("Unauthorized");
            return new Gson().toJson(message);
        }
    }

    private Object createGame(Request request, Response response) {
        String authToken = request.session().attribute("authorization");
        if (service.validToken(authToken)) {
            try {
                int id = service.createGame(new Gson().fromJson(request.body(), String.class));
                response.status(200);
                return new Gson().toJson(new GameID(id));
            } catch (DataAccessException dae) {
                response.status(400);
                return new Gson().toJson(new Message("Error: bad request"));
            }
        } else {
            response.status(401);
            Message message = new Message("Unauthorized");
            return new Gson().toJson(message);
        }
    }

    private String listGames(Request request, Response response) {
        String authToken = request.session().attribute("authorization");
        if (service.validToken(authToken)) {
            response.status(200);
            return new Gson().toJson(service.listGames());
        }
        response.status(401);
        Message message = new Message("Unauthorized");
        return new Gson().toJson(message);
    }

    private String logout(Request request, Response response) throws DataAccessException {
        String authToken = request.session().attribute("authorization");
        if (service.logout(authToken)) {
            response.status(200);
            return "";
        } else {
            response.status(401);
            Message message = new Message("Unauthorized");
            return new Gson().toJson(message);
        }
    }

    private String login(Request request, Response response) throws DataAccessException {
        UsernameAndPassword NameAndPass = new Gson().fromJson(request.body(), UsernameAndPassword.class);
        String username = NameAndPass.username();
        String password = NameAndPass.password();
        UserData storedData = service.findDataByUnPwd(username, password);
        if (storedData != null) {
            String authToken = service.createAuthToken(storedData);
            response.status(200);
            AuthData authData = new AuthData(authToken, username);
            return new Gson().toJson(authData);
        } else {
            response.status(401);
            Message message = new Message("Unauthorized");
            return new Gson().toJson(message);
        }
    }

    private String register(Request request, Response response) throws DataAccessException {
        UserData newUser = new Gson().fromJson(request.body(), UserData.class);
        service.createUser(newUser);
        response.status(200);
        String authToken = service.createAuthToken(newUser);
        return new Gson().toJson(new AuthData(authToken,newUser.username()));
    }


    public String clear(Request req, Response res) {
        service.clear();
        res.status(200);
        return "";
    }

    public Integer port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}


