package ui;

import java.util.Scanner;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final PreLoginClient prelogin;
    private final PostLoginClient postlogin;
    private final ChessClient chessClient;
    private String status;

    public Repl(String serverUrl) {

        chessClient = new ChessClient(serverUrl, this);
        prelogin = new PreLoginClient(serverUrl, this);
        postlogin = new PostLoginClient(serverUrl, this);
        status = "[LOGGED OUT]";
    }

    public void run() {
        System.out.println(prelogin.welcome());
        System.out.println(prelogin.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(SET_TEXT_COLOR_RED+"quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = prelogin.eval(line);
                System.out.println(result);
                if (result == SET_TEXT_COLOR_GREEN+"success"){
                    status = "[LOGGED IN]";
                    loggedIn();
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void loggedIn() {
        Scanner scanner = new Scanner(System.in);
        var outcome = "";
        while (!outcome.equals("quit")) {
            System.out.println(postlogin.help());
            printPrompt();
            String line = scanner.nextLine();
            try {
                outcome = postlogin.eval(line);
                System.out.print(outcome);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" +SET_TEXT_COLOR_MAGENTA + status + RESET_TEXT_BLINKING + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}


