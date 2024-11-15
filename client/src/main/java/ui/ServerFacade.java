package ui;
import chess.ChessGame;
import com.google.gson.Gson;
import com.sun.net.httpserver.Request;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.ColorAndGame;
import server.GameID;
import server.GamesList;
import server.UsernameAndPassword;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static ui.PostLoginClient.authToken;


public class ServerFacade {
    String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }
//    public void observe(int gameID){
//        var path = "/game";
//        ColorAndGame request = new ColorAndGame(null, gameID);
//        this.makeRequest("PUT", path, request, null);
//    }

    public void join(ChessGame.TeamColor color, int gameID){
        var path = "/game";
        ColorAndGame request = new ColorAndGame(color, gameID);
        this.makeRequest("PUT", path, request, null);
    }

    public ArrayList<GameData> listGames() {
        var path = "/game";
        GamesList response = this.makeRequest("GET", path, null, GamesList.class);
        return response.games();
    }

    public GameID createGame(String gameName){
        var path = "/game";
        GameData allNullButName = new GameData(0, null, null, gameName, null);
        return this.makeRequest("POST", path, allNullButName, GameID.class);
    }

    public void logout(){
        var path = "/session/";
        this.makeRequest("DELETE", path, null, null);
    }
    public String login(String username, String password){
        var path = "/session";
        var request = new UsernameAndPassword(username, password);
        var recieved = this.makeRequest("POST", path, request, AuthData.class);
        return recieved.authToken();
    }

    public String register(String username, String password, String email){
        var path = "/user";
        var request = new UserData(username, password, email);
        var recieved = this.makeRequest("POST", path, request, AuthData.class);
        return recieved.authToken();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("authorization", authToken);
            writeBody(request, http);
            http.connect();
            return readBody(http, responseClass);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        if (responseClass == null) {
            return null;
        }
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
