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
                case "logout" -> logoutExistingUser(params);
                case "register" -> registerNewUser(params);
                case "quit" -> SET_TEXT_COLOR_RED+"quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String logoutExistingUser(String[] params) {
    }

    public String help() {
        return "You are logged in. Your options are:\n" +
                "--logout\n--join a game\n--observe a game\n--";
    }
}
