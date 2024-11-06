package server;

import com.google.gson.Gson;
import dataaccess.UserDAO;
import dataaccess.memory.MemAuth;
import dataaccess.memory.MemGame;
import dataaccess.memory.MemUser;
import dataaccess.sql.SqlAuth;
import dataaccess.sql.SqlGame;
import dataaccess.sql.SqlUser;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.Service;
import spark.*;
import dataaccess.*;

import java.sql.SQLException;


public class Server {
    UserDAO userDataAccess;
    GameDAO gameDataAccess;
    AuthDAO authDataAccess;

    private final Service service;

    public Server() {
        try {
            userDataAccess = new SqlUser();
            gameDataAccess = new SqlGame();
            authDataAccess = new SqlAuth();
            service = new Service(userDataAccess, authDataAccess, gameDataAccess);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::join);
        Spark.awaitInitialization();
        return Spark.port();
    }


    private String join(Request request, Response response) throws SQLException, DataAccessException {
        String authToken = request.headers("authorization");
        if (service.validToken(authToken)) {
            try {
                ColorAndGame cag = new Gson().fromJson(request.body(), ColorAndGame.class);
                if(cag.playerColor()==null || cag.gameID() == 0){
                    response.status(400);
                    return new Gson().toJson(new Message("Error: bad request"));
                }
                service.joinGame(authToken, cag.playerColor(), cag.gameID());
                response.status(200);
                return "";
            } catch (DataAccessException dae) {
                if (dae.getMessage().equals("Error: already taken")) {
                    response.status(403);
                } else {
                    response.status(400);
                }
                return new Gson().toJson(new Message("Error: bad request"));
            }
        } else {
            response.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        }
    }

    private Object createGame(Request request, Response response) {
        String authToken = request.headers("authorization");
        //System.out.println(service.validToken(authToken));

        if (service.validToken(authToken)) {
            try {
                //System.out.println("test me, ben!");
                GameData allNullButName = new Gson().fromJson(request.body(), GameData.class);
                //System.out.println(service.createGame(allNullButName.gameName()));
                int id = service.createGame(allNullButName.gameName());
                response.status(200);
                return new Gson().toJson(new GameID(id));
            } catch (DataAccessException dae) {
                //System.out.println(dae.getMessage());
                response.status(400);
                return new Gson().toJson(new Message("Error: bad request"));
            }
        } else {
            response.status(401);
            Message message = new Message("Error: unauthorized");
            return new Gson().toJson(message);
        }
    }

    private String listGames(Request request, Response response) {
        String authToken = request.headers("authorization");
        if (service.validToken(authToken)) {

            try {
                response.status(200);
                GamesList gameList = new GamesList(service.listGames());
                return new Gson().toJson(gameList);
            } catch (Exception e) {
                response.status(500);
                return new Gson().toJson(new Message(e.getMessage()));
            }
        } else {
            response.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        }
    }

    private String logout(Request request, Response response) {
        String authToken = request.headers("authorization");
        if (service.validToken(authToken)) {
            try {
                service.logout(authToken);
                response.status(200);
                return "";
            } catch (Exception e) {
                response.status(500);
                return new Gson().toJson(new Message(e.getMessage()));
            }
        } else {
            response.status(401);
            return new Gson().toJson(new Message("Error: unauthorized"));
        }
    }

    private String login(Request request, Response response) {
        try {
            UsernameAndPassword nameAndPass = new Gson().fromJson(request.body(), UsernameAndPassword.class);
            String username = nameAndPass.username();
            String password = nameAndPass.password();

            UserData storedData = service.findDataByUnPwd(username, password);
            String authToken = service.createAuthToken(storedData);
            response.status(200);
            AuthData authData = new AuthData(authToken, username);
            return new Gson().toJson(authData);
        } catch (DataAccessException dae) {
            String complaint = dae.getMessage();
            if (complaint == "Error: unauthorized") {
                response.status(401);
            } else {
                response.status(500);
            }
            return new Gson().toJson(new Message(complaint));
        }
    }

    private String register(Request request, Response response) {
        UserData newUser = null;
        newUser = new Gson().fromJson(request.body(), UserData.class);
        if (newUser.password() == null || newUser.username() == null) {
            response.status(400);
            return new Gson().toJson(new Message("Error: bad request"));
        }
        try {
            service.createUser(newUser);
            response.status(200);
            String authToken = service.createAuthToken(newUser);
            return new Gson().toJson(new AuthData(authToken, newUser.username()));
        } catch (DataAccessException e) {
            String complaint = e.getMessage();
            if (complaint == "Error: already taken") {
                response.status(403);
            } else {
                response.status(500);
            }
            return new Gson().toJson(new Message(complaint));
        }
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


