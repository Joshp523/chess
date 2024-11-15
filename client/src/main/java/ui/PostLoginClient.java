package ui;

import chess.ChessGame;
import model.GameData;
import server.GameID;

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

    PostLoginClient(String serverURL, Repl repl) {
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
                case "play" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "help" -> help();
                case "quit" -> SET_TEXT_COLOR_RED + "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String observeGame(String[] params) {
        int index = Integer.parseInt(params[0]);
        return printBoard(index);
    }

    private String joinGame(String[] params) {
        ChessGame.TeamColor color = null;
        switch (params[1]) {
            case "WHITE":
                color = ChessGame.TeamColor.WHITE;
                break;
            case "BLACK":
                color = ChessGame.TeamColor.BLACK;
        }
        int index = Integer.parseInt(params[0]);
        server.join(color, index);
        return printBoard(index);
    }

    private String printBoard(int index) {
        ChessGame printMe= temporaryEnumeration.get(index);
        String blackSide = "";
        String whiteSide = "";
        return whiteSide +"\n"+blackSide;
    }

    private String listGames() {
        ArrayList<GameData> games = server.listGames();
        String returnMe = SET_TEXT_COLOR_BLUE + "here are all the games:\n";
        int i = 1;
        for (GameData datum : games) {
            returnMe += i + ". " + datum.gameName() + "\n\tWhite: " + datum.whiteUsername() +
                    "\n\tBlack: " + datum.blackUsername() + "\n";
            temporaryEnumeration.put(i,datum.game());
            i++;
        }
        return returnMe;
    }

    private String createGame(String[] params) {
        server.createGame(params[0]);
        return SET_TEXT_COLOR_BLUE + "a game called " + params[0] + " was created.";
    }

    private String logoutExistingUser() {
        this.authToken = "";
        server.logout();
        return "goodbye";
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
                "--To quit, please enter \"quit\"";
    }
}
