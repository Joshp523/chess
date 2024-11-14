package ui;
import chess.ChessGame;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class ServerFacade {
    String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ChessGame[] listGames() {
        var path = "/game";
        record listGamesResponse(ChessGame[] games) {
        }
        var response = this.makeRequest("GET", path, null, listGamesResponse.class);
        return response.games();
    }
    public String join(){
        var path = "/game";
        return this.makeRequest("PUT", path, null, String.class);
    }
    public Object createGame(String gameName){
        var path = "/game";
        return this.makeRequest("POST", path, null, String.class);
    }
    public String logout(){
        var path = "/session";
        return this.makeRequest("DELETE", path, null, String.class);
    }
    public String login(){
        var path = "/session";
        return this.makeRequest("GAME", path, null, String.class);
    }

    public String register(){
        var path = "/user";
        return this.makeRequest("POST", path, null, String.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
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
