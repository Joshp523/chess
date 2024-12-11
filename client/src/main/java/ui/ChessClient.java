package ui;

import chess.*;
import model.BoardAndMessage;
import model.GameData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.websocket.*;

import static ui.EscapeSequences.*;

public class ChessClient {
    public Session session;
    public BoardAndMessage bam;
    private WebSocketFacade ws;
    int gameID;
    ServerFacade server;
    ChessGame.TeamColor color;

    ChessClient(String serverURL, MessageHandler messageHandler, String token, int id, ChessGame.TeamColor color) throws Exception {
        server = new ServerFacade(serverURL, token);
        ws = new WebSocketFacade(serverURL, messageHandler, token, id, color, this);
        gameID = id;
        this.color = color;
    }

    private ChessGame getGameFromID(int id){
        for (GameData data: server.listGames()){
            if (data.gameID()==id){
                return data.game();
            }
        }
        return null;
    }

    public String eval(String input) {
        try {

            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> printBoard();
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

    private String quitter() throws IOException {
        ws.resign();
        return SET_TEXT_COLOR_RED + "you forfeited the game\n--To exit the game, please enter \"leave\"\n";
    }

    private String exitGame() throws IOException {
        ws.leaveGame();
        return SET_TEXT_COLOR_RED + "you left the game";
    }

    private ChessPosition inputToPosition(String input) {
        char alphaCol = input.charAt(0);
        char charRow = input.charAt(1);
        int col;
        int row = Character.getNumericValue(charRow);
        switch (alphaCol) {
            case 'a' -> col = 1;
            case 'b' -> col = 2;
            case 'c' -> col = 3;
            case 'd' -> col = 4;
            case 'e' -> col = 5;
            case 'f' -> col = 6;
            case 'g' -> col = 7;
            default -> col = -1;
        }
        return new ChessPosition(row, col);
    }

    private String showLegalMoves(String[] params) throws Exception {
        ChessPosition square = inputToPosition(params[0]);
        Collection<ChessMove> legalMoves = getGameFromID(gameID).validMoves(square);
        AnnotatedChessBoard movesMarked = new AnnotatedChessBoard(getGameFromID(gameID).getBoard(), legalMoves);
        PrintBoard.main(movesMarked, color);
        return SET_TEXT_COLOR_BLUE + "valid moves for this piece marked in green.\n";
    }

    private String move(String[] params) throws IOException {
        ws.makeMove(new ChessMove(inputToPosition(params[0]), inputToPosition(params[1]),null));
        return SET_TEXT_COLOR_BLUE + "you moved";
    }

    public String printBoard() {
        PrintBoard.main(new AnnotatedChessBoard(getGameFromID(gameID).getBoard(),null), color);
        return SET_TEXT_COLOR_BLUE+ "current gameboard\n";
    }
    public String printBoard(ChessBoard board) {
        return PrintBoard.main(new AnnotatedChessBoard(board,null), color);
    }
}
