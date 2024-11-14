package ui;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PostLoginClient {
    public static String authToken = "";
    ServerFacade server;
    String serverUrl;

    PostLoginClient(String serverURL, Repl repl){
        this.serverUrl = serverURL;
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logoutExistingUser();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "help" -> help();
                case "quit" -> SET_TEXT_COLOR_RED+"quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String observeGame(String[] params) {
        return "NOT IMPLEMENTED";
    }

    private String joinGame(String[] params) {
        return "NOT IMPLEMENTED";
    }

    private String listGames() {
        return "NOT IMPLEMENTED";
    }

    private String createGame(String[] params) {
        return "NOT IMPLEMENTED";
    }

    private String logoutExistingUser() {
        this.authToken = "";
        server.logout();
        return "goodbye";
    }

    public String welcome() {
        return "Select an option below";
    }

    public String help(){
        return SET_TEXT_COLOR_YELLOW + "--To log out, please enter \"logout\" \n" +
                "--To create a new game, please enter \"create\" <GAMENAME>\n" +
                "--To list all the current games, please enter \"list\"\n" +
                "--To join a game, please enter \"join\" <GAMENAME>\n" +
                "--To observe a game, please enter \"observe\" <GAMENAME>\n" +
                "--To see this menu again, please enter \"help\"\n" +
                "--To quit, please enter \"quit\"";
    }
}
