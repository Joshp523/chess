package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static chess.ChessGame.TeamColor.WHITE;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;
    private final ArrayList<ChessBoard> history = new ArrayList<ChessBoard>();
    boolean whiteCM;
    boolean blackCM;
    boolean whiteSM;
    boolean blackSM;
    boolean over;

    public ChessGame() {
        this.turn = WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.whiteCM = false;
        this.blackCM = false;
        this.whiteSM = false;
        this.blackSM = false;
        this.over = false;
        updateStatus();
    }

    public String gameOver(){
        if(whiteCM){
            over = true;
            return "Black wins!\nGame over.";}
        if(blackCM){
            over = true;
            return "White wins!\nGame over.";}
        if(whiteSM || blackSM){
            over = true;
            return "Stalemate!\nGame over.";}
        if(over){return "";}
        else{return null;}

    }

    public void resign(){
        over = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(getBoard(), chessGame.getBoard()) && Objects.equals(history, chessGame.history);
    }

    @Override
    public int hashCode() {

        return Objects.hash(turn, getBoard(), history);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
        updateStatus();
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        GREEN, BLACK
    }

    private boolean suicide(ChessMove move, ChessBoard workingBoard) {
        ChessBoard potentialBoard = workingBoard;
        ChessPiece pieceInQuestion = workingBoard.getPiece(move.getStartPosition());
        boolean check = false;
        if (pieceInQuestion != null) {
            ChessPiece enemy = workingBoard.getPiece(move.getEndPosition());
            potentialBoard.removePiece(move.getStartPosition());
            potentialBoard.addPiece(move.getEndPosition(), pieceInQuestion);
            check = isInCheck(pieceInQuestion.getTeamColor(), potentialBoard);
            potentialBoard.removePiece(move.getEndPosition());
            potentialBoard.addPiece(move.getStartPosition(), pieceInQuestion);
            potentialBoard.addPiece(move.getEndPosition(), enemy);
        }
        return check;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return validMoves(startPosition, this.board);
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition, ChessBoard testBoard) {
        ArrayList<ChessMove> finalizedMoves = new ArrayList<>();
        if (testBoard.getPiece(startPosition) == null) {
            return null;
        } else {
            for (ChessMove move : testBoard.getPiece(startPosition).pieceMoves(testBoard, startPosition)) {
                if (!suicide(move, testBoard)) {
                    finalizedMoves.add(move);
                }
            }
        }
        return finalizedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException();
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException();
        } else if (board.getPiece(move.getStartPosition()).getTeamColor() != turn) {
            throw new InvalidMoveException();
        } else {
            ChessBoard newBoard = this.board;
            ChessPiece pieceInQuestion = this.board.getPiece(move.getStartPosition());
            newBoard.removePiece(move.getStartPosition());
            ChessPiece promoPiece = new ChessPiece(pieceInQuestion.getTeamColor(), move.getPromotionPiece());
            if (move.getPromotionPiece() != null) {
                newBoard.addPiece(move.getEndPosition(), promoPiece);
            } else {
                newBoard.addPiece(move.getEndPosition(), pieceInQuestion);
            }
            this.history.add(this.board);
            setBoard(newBoard);
            if (this.turn == WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(WHITE);
            }
            updateStatus();
        }
    }

    private ChessPosition findKing(TeamColor teamColor, ChessBoard testBoard) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                if (testBoard.getPiece(testPos) != null) {
                    if (testBoard.getPiece(testPos).getPieceType() == ChessPiece.PieceType.KING
                            && testBoard.getPiece(testPos).getTeamColor() == teamColor) {
                        return testPos;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, this.board);
    }


    private boolean isInCheck(TeamColor teamColor, ChessBoard testBoard) {

        ChessPosition kingPos = findKing(teamColor, testBoard);
        ChessPosition enemyPos;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = testBoard.getPiece(testPos);
                if (testPiece != null) {
                    if (extracted(teamColor, testBoard, testPiece, testPos, kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean extracted(TeamColor teamColor, ChessBoard testBoard, ChessPiece testPiece, ChessPosition testPos, ChessPosition kingPos) {
        ChessPosition enemyPos;
        if (testPiece.getTeamColor() != teamColor) {
            enemyPos = testPos;
            for (ChessMove potentialMove : testPiece.pieceMoves(testBoard, enemyPos)) {
                if (potentialMove.getEndPosition().equals(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        switch (teamColor) {
            case WHITE:
                return whiteCM;
            case BLACK:
                return blackCM;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        switch (teamColor) {
            case WHITE:
                return whiteSM;
            case BLACK:
                return blackSM;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        updateStatus();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private boolean stalemateChecker() {
        if (isInCheck(turn)) {
            return false;
        }
        return kingInDanger();
    }

    private boolean kingInDanger() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = board.getPiece(testPos);
                if (testPiece != null) {
                    if (testPiece.getTeamColor() == turn && !validMoves(testPos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkmateChecker() {
        if (!isInCheck(turn)) {
            return false;
        }
        return kingInDanger();
    }

    private void updateStatus() {
        switch (turn) {
            case WHITE:
                whiteSM = stalemateChecker();
                whiteCM = checkmateChecker();
                break;
            case BLACK:
                blackSM = stalemateChecker();
                blackCM = checkmateChecker();
                break;
        }
    }
}