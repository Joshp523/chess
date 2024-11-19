package ui;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.ColorAndGame;
import model.GameID;
import model.GamesList;
import model.UsernameAndPassword;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class ServerFacade {
    String serverUrl;
    String authToken;

    public ServerFacade(String url, String authTok) {
        serverUrl = url;
        authToken = authTok;
    }

    public void join(ChessGame.TeamColor color, int gameID){
        //System.out.println("join function in serverFacade reached");
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
        if (authToken == null){
            throw new RuntimeException("You are not logged in");
        }
        var path = "/session";
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
            //System.out.println("makeRequest reached");
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("authorization", authToken);
            writeBody(request, http);
            http.getResponseCode();
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        //System.out.println("writeBody reached");
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            OutputStream reqBody = http.getOutputStream();
            reqBody.write(reqData.getBytes());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        //System.out.println("readBody reached");
        if (responseClass != null) {
            if (http.getContentLength() < 0) {
                InputStream respBody = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(respBody);
                return new Gson().fromJson(reader, responseClass);
            }
        }
        return null;
    }
}
