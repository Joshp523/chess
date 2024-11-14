package ui;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class PostLoginClient {
    public static String authToken = "";

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

    public String welcome() {
        return "You are logged in.";
    }

    public String help(){
        return SET_TEXT_COLOR_YELLOW + "--To log out, please enter\t \"logout\" \n" +
                "--To create a new game, please enter\t \"create\" \t<GAMENAME>\n" +
                "--To list all the current games, please enter\t \"list\"\n" +
                "--To join a game, please enter\t \"join\" \t<GAMENAME>\n" +
                "--To observe a game, please enter\t \"observe\" \t<GAMENAME>\n" +
                "--To see this menu again, please enter\t \"help\"\n" +
                "--To quit, please enter\t\t \"quit\"";
    }
}
