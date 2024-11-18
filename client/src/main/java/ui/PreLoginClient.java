package ui;

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
        return SET_TEXT_COLOR_YELLOW + "--To register, please enter \"register\" <USERNAME> <PASSWORD> <EMAIL>\n" +
                "--To login, please enter \"login\" <USERNAME> <PASSWORD>\n" +
                "--To see this menu again, please enter \"help\"\n" +
                "--To exit this program, please enter \"quit\"";
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
                case "help" -> help();
                default -> SET_TEXT_COLOR_RED + "Invalid input command\nPlease try again in the proscribed format";
            };
        } catch (Exception ex) {
            return SET_TEXT_COLOR_RED + "Something's not right. Please try again.";
        }
    }

    private String registerNewUser(String... params) {
        if (params.length < 3) {
            return SET_TEXT_COLOR_RED + "missing argument(s).\nPlease try again.";
        }if(params.length > 3){
            return SET_TEXT_COLOR_RED + "too many argument(s)\nPlease try again.";
        }
        try {
            String authToken = server.register(params[0], params[1], params[2]);
            if (authToken.equals("")) {
                return SET_TEXT_COLOR_RED + "The username you entered is already taken.\nPlease try a different one.";
            }else {
                PostLoginClient.authToken = authToken;
                return SET_TEXT_COLOR_GREEN+"successfully logged in";
            }
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Something's not right :/\nPlease try again.";
        }
    }

    private String loginExistingUser(String[] params) {
        if (params.length < 2) {
            return SET_TEXT_COLOR_RED + "missing argument(s).\nPlease try again.";
        }if(params.length > 2){
            return SET_TEXT_COLOR_RED + "too many argument(s)\nPlease try again.";
        }
        try {
            String authToken = server.login(params[0], params[1]);
            if (authToken.isEmpty()) {
                return SET_TEXT_COLOR_RED + "incorrect password.";
            }else {
                PostLoginClient.authToken = authToken;
                return SET_TEXT_COLOR_GREEN+"successfully logged in";
            }
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "login failed. Please doublecheck your password.\nIf you don't have an account with us, please register.";
        }
    }
}
