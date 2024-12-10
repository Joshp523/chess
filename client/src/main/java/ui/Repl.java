package ui;

import chess.ChessBoard;

import java.util.Scanner;

import static ui.EscapeSequences.*;

import com.google.gson.Gson;
import server.Message;
import websocket.commands.*;
import websocket.messages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Repl implements MessageHandler {
    private final PreLoginClient prelogin;
    private String status;
    String serverURL;

    public Repl(String serverUrl) {
        this.serverURL = serverUrl;
        prelogin = new PreLoginClient(serverUrl, this);
        status = "[LOGGED OUT]";
    }

    public void run() {
        System.out.println(prelogin.welcome());
        System.out.println(prelogin.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(SET_TEXT_COLOR_RED + "quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = prelogin.eval(line);
                int newline = result.indexOf("\n");
                if (result.contains("success")) {
                    String firstHalf = result.substring(0, newline);
                    String secondHalf = result.substring(newline + 1);
                    System.out.println(firstHalf);
                    status = "[LOGGED IN]";
                    PostLoginClient postlogin = new PostLoginClient(prelogin.serverUrl, this, secondHalf);
                    loggedIn(postlogin);
                } else {
                    System.out.println(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void loggedIn(PostLoginClient postlogin) {
        System.out.println(postlogin.welcome());
        System.out.println(postlogin.help());
        Scanner scanner = new Scanner(System.in);
        var outcome = "";
        while (status == "[LOGGED IN]") {
            printPrompt();
            String line = scanner.nextLine();
            try {
                outcome = postlogin.eval(line);
                System.out.print(outcome);
                if (outcome.equals("goodbye")) {
                    status = "[LOGGED OUT]";
                }
                if (outcome.contains("joined") || outcome.contains("observing")) {
                    ChessClient client = new ChessClient(serverURL, this);
                    playGame(client);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void playGame(ChessClient client) throws Exception {
        System.out.println(client.help());

        Scanner scanner = new Scanner(System.in);
        var outcome = "";
        var ws = new ChessClient(serverURL, this);
        System.out.println(client.printBoard(new ChessBoard(), ws.getPlayerColor()));
        do {
            printPrompt();
            String line = scanner.nextLine();
            outcome = ws.eval(line);
            System.out.print(outcome);
        } while (!outcome.equals("you left"));
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_MAGENTA + status + RESET_TEXT_BLINKING + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public void notify(Message message) {
        System.out.println(SET_TEXT_COLOR_BLUE + message.message());

    }

}


