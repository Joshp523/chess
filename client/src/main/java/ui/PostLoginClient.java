package ui;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;

public class PostLoginClient {
    public static String authToken = "";
    ServerFacade server;
    String serverUrl;
    Map<Integer, ChessGame> temporaryEnumeration = new HashMap<>();
    PrintBoard squares;

    PostLoginClient(String serverURL, Repl repl) {
        this.serverUrl = serverURL;
        server = new ServerFacade(serverUrl);
        squares = new PrintBoard();
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
                case "play" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "help" -> help();
                case "quit" -> SET_TEXT_COLOR_RED + "you left the game";
                default -> SET_TEXT_COLOR_RED + "Invalid input command\nPlease try again in the proscribed format";
            };
        } catch (Exception ex) {
            return SET_TEXT_COLOR_RED + "Something's not right. Please try again.";
        }
    }

    private String observeGame(String[] params) {
        printBoard();
        return "";
    }

    private String joinGame(String[] params) {
        ChessGame.TeamColor color = null;
        if (params.length < 2) {
            return SET_TEXT_COLOR_RED + "missing argument(s).\nPlease try again.";
        }if(params.length > 2){
            return SET_TEXT_COLOR_RED + "too many argument(s)\nPlease try again.";
        }
        try {
            color = switch (params[1]) {
                case "WHITE" -> ChessGame.TeamColor.WHITE;
                case "BLACK" -> ChessGame.TeamColor.BLACK;
                default -> color;
            };
            int index = Integer.parseInt(params[0]);
            server.join(color, index);
            printBoard();
            return "";
        } catch (NumberFormatException e) {
            return SET_TEXT_COLOR_RED + "Something's not right :/\n make sure to type the number of the game from the list and your desired color in all caps.";
        }
    }

    private void printBoard() {
        squares.main();
    }

    private String listGames() {
        try {
            ArrayList<GameData> games = server.listGames();
            String returnMe = SET_TEXT_COLOR_BLUE + "here are all the games:\n";
            int i = 1;
            for (GameData datum : games) {
                returnMe += i + ". " + datum.gameName() + "\n\tWhite: " + datum.whiteUsername() +
                        "\n\tBlack: " + datum.blackUsername() + "\n";
                temporaryEnumeration.put(i, datum.game());
                i++;
            }
            return returnMe;
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Something's not right :/\nPlease try again.";

        }
    }

    private String createGame(String[] params) {
        if (params.length < 1) {
            return SET_TEXT_COLOR_RED + "missing argument(s).\nPlease try again.";
        }if(params.length > 1){
            return SET_TEXT_COLOR_RED + "too many argument(s)\nPlease try again.";
        }
        try {
            server.createGame(params[0]);
            return SET_TEXT_COLOR_BLUE + "a game called " + params[0] + " was created.";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Something's not right :/\nPlease try again.";
        }
    }

    private String logoutExistingUser() {
        try {
            this.authToken = "";
            server.logout();
            return "goodbye";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Something's not right :/\nPlease try again.";
        }
    }

    public String welcome() {
        return "Select an option below";
    }

    public String help() {
        return SET_TEXT_COLOR_YELLOW + "--To log out, please enter \"logout\" \n" +
                "--To create a new game, please enter \"create\" <GAMENAME>\n" +
                "--To list all the current games, please enter \"list\"\n" +
                "--To play in one of the listed games, please enter \"play\" <GAME NUMBER> [WHITE/BLACK]\n" +
                "--To observe a game, please enter \"observe\" <GAME NUMBER>\n" +
                "--To see this menu again, please enter \"help\"\n" +
                "--To leave a game you are in, please enter \"quit\"";
    }
}
