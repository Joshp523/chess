package ui;

import dataaccess.DataAccessException;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PreLoginClient {
    ServerFacade server;
    String serverUrl;


    PreLoginClient(String serverURL, Repl repl) {
        this.serverUrl = serverURL;
        server = new ServerFacade(serverUrl);

    }

    public String welcome() {
        return SET_TEXT_COLOR_YELLOW + "Welcome to Joshua's chess server.\nplease create an account or login\n";
    }

    public String help() {
        return SET_TEXT_COLOR_YELLOW + "--To register, please enter\t \"register\" <USERNAME> <PASSWORD> <EMAIL>\n" +
                "--To login, please enter\t \"login\" \t<USERNAME> <PASSWORD>\n" +
                "--To quit, please enter\t\t \"quit\"";
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> loginExistingUser(params);
                case "register" -> registerNewUser(params);
                case "quit" -> SET_TEXT_COLOR_RED + "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String registerNewUser(String... params) {
        String authToken = server.register(params[0], params[1], params[2]);
        if (authToken.equals("")) {
            return SET_TEXT_COLOR_RED + "failure";
        }else {

            return SET_TEXT_COLOR_GREEN + "success";
        }
    }

    private String loginExistingUser(String[] params) {
        return SET_TEXT_COLOR_RED + "NOT IMPLEMENTED";
    }
}
