package ui;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PreLoginClient {

    PreLoginClient(String serverURL, Repl repl) {
    }

    public String welcome() {
        return SET_TEXT_COLOR_YELLOW + "Welcome to Joshua's chess server.\nplease create an account or login";
    }

    public String help() {
        return "--To register, please enter \"register\" <PASSWORD> <EMAIL>\n" +
                "--To login, please enter \"login\" <PASSWORD>\n" +
                "--To quit, please enter \"quit\"";
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> loginExistingUser(params);
                case "register" -> registerNewUser(params);
                case "quit" -> SET_TEXT_COLOR_RED+"quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String registerNewUser(String... params) {
        if (params.length >= 1) {
            visitorName = String.join("-", params);
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Expected: <yourname>");

    }

    private String loginExistingUser(String[] params) {
    }
}
