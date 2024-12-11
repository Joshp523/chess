package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import model.BoardAndMessage;
import java.io.IOException;
import java.util.Arrays;
import javax.websocket.*;

import static ui.EscapeSequences.*;

public class ChessClient {
    public Session session;
    ChessBoard board;
    public BoardAndMessage bam;
    private WebSocketFacade ws;
    String authToken;
    int gameID;
    ServerFacade server;

    ChessClient(String serverURL, MessageHandler messageHandler, String token, int id) throws Exception {
        server = new ServerFacade(serverURL, token);
        ws = new WebSocketFacade(serverURL, messageHandler, token, id);
        gameID = id;
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

    private String areYouSure() {
        return SET_TEXT_COLOR_RED + "Are you sure you want to resign?\nType yes or no";
    }

    public String help() {
        return STR."""
\{SET_TEXT_COLOR_YELLOW}--To forfeit the game, please enter "resign"\s
--To to see the current game board again, please enter "redraw"
--To exit the game, please enter "leave"
--To move, please enter "move" <STARTING SQUARE> <ENDING SQUARE>
--To view all legal moves for a piece, please enter "highlight" <SQUARE>
--To see this menu again, please enter "help"
""";
    }

    public String getPlayerColor() {
        return "w";
    }


    private String quitter() throws IOException {
        ws.resign(authToken, gameID);
        return SET_TEXT_COLOR_RED + "you forfeited the game\n--To exit the game, please enter \\\"leave\\\"\\n\"";
    }

    private String exitGame() throws IOException {
        ws.leaveGame(authToken, gameID);
        return SET_TEXT_COLOR_RED + "you left the game";
    }

    private String showLegalMoves(String[] params) throws Exception {
        int row = (int)params[0].charAt(0);
        char charCol = params[1].charAt(0);
        int col;
        switch (charCol) {
            case 'A' -> col = 1;
            case 'B' -> col = 2;
            case 'C' -> col = 3;
            case 'D' -> col = 4;
            case 'E' -> col = 5;
            case 'F' -> col = 6;
            case 'G' -> col = 7;
            default -> col = -1;
        }
        ChessPosition square = new ChessPosition(row,col);
        ChessGame game= getGameFromID();
        return printLegalMoves(game, square);
    }

    private String printLegalMoves(ChessGame game, ChessPosition square) {
        return "not implemented";
    }

    private ChessGame getGameFromID() {
        return new ChessGame();
    }

    private String move(String[] params) throws IOException {
        this.session.getBasicRemote().sendText("move");
        return SET_TEXT_COLOR_BLUE + "you moved";
    }

    public String printBoard(ChessBoard board, String perspective) {
        return PrintBoard.main(board, perspective);
    }
}
