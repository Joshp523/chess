package ui;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class PostLoginClient {

    PostLoginClient(String serverURL, Repl repl){

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
        return "NOT IMPLEMENTED";
    }

    public String help() {
        return "You are logged in. Your options are:\n" +
                "--logout\n--join a game\n--observe a game\n--";
    }
}
