package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.BoardAndMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.websocket.*;

import static ui.EscapeSequences.*;

public class ChessClient {
    URI uri;
    public Session session;
    ChessBoard board;
    public BoardAndMessage bam;

    ChessClient(String serverURL, MessageHandler notificationHandler) throws Exception {
        this.uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, this.uri);
        board = new ChessBoard();
        this.session.addMessageHandler(new MessageHandler.Whole<BoardAndMessage>() {
            public void onMessage(BoardAndMessage bam) {
                /* not sure what goes here*/
            }
        });
    }

    public String eval(String input) {
        try {

            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> printBoard(board, this.getPlayerColor());
                case "leave" -> exitGame();
                case "move" -> move(params);
                case "resign" -> areYouSure();
                case "yes" -> quitter();
                case "highlight" -> showLegalMoves(params);
                case "help" -> help();
                case "quit" -> SET_TEXT_COLOR_RED + "you left the game";
                default -> SET_TEXT_COLOR_RED + "Invalid input command";
            };
        } catch (Exception ex) {
            return SET_TEXT_COLOR_RED + "Something's not right. Please try again.";
        }
    }


    public String getPlayerColor() {
        return "w";
    }

    private String areYouSure() {
        return SET_TEXT_COLOR_RED + "Are you sure you want to resign?\nType yes or no";
    }

    private String showLegalMoves(String[] params) throws Exception {
        this.session.getBasicRemote().sendText(params[0]);
        return printBoard(board, this.getPlayerColor());
    }

    private String quitter() throws IOException {
        this.session.getBasicRemote().sendText("resign");

        return SET_TEXT_COLOR_RED + "you lost the game\n--To exit the game, please enter \\\"leave\\\"\\n\"";
    }

    private String move(String[] params) throws IOException {
        this.session.getBasicRemote().sendText("move");
        return SET_TEXT_COLOR_BLUE + "you moved";
    }

    private String exitGame() {
        return SET_TEXT_COLOR_RED + "you left the game";
    }

    public String help() {
        return SET_TEXT_COLOR_YELLOW + "--To forfeit the game, please enter \"resign\" \n" +
                "--To to see the current game board again, please enter \"redraw\"\n" +
                "--To exit the game, please enter \"leave\"\n" +
                "--To move, please enter \"move\" <STARTING SQUARE> <ENDING SQUARE>\n" +
                "--To view all legal moves for a piece, please enter \"highlight\" <SQUARE>\n" +
                "--To see this menu again, please enter \"help\"\n";
    }

    public String printBoard(ChessBoard board, String perspective) {
        return PrintBoard.main(board, perspective);
    }
}
