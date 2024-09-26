package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;
    private ArrayList<ChessBoard> history = new ArrayList<ChessBoard>();

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.board = new ChessBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private boolean suicide(ChessMove move, ChessBoard workingBoard) {
        ChessBoard potential = workingBoard;
        ChessPiece pieceInQuestion = workingBoard.getPiece(move.getStartPosition());
        potential.addPiece(move.getStartPosition(), null);
        potential.addPiece(move.getEndPosition(), pieceInQuestion);
        return isPotentiallyInCheck(pieceInQuestion.getTeamColor(), potential);
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> finalizedMoves = new ArrayList();
        if (this.board.getPiece(startPosition) == null) return null;
        else {
            for (ChessMove move : this.board.getPiece(startPosition).pieceMoves(board, startPosition)) {
                if (!suicide(move, this.board)) finalizedMoves.add(move);
            }
        }
        return finalizedMoves;
    }

    public Collection<ChessMove> validHypotheticalMoves(ChessPosition startPosition, ChessBoard testBoard) {
        ArrayList<ChessMove> finalizedMoves = new ArrayList();
        if (testBoard.getPiece(startPosition) == null) return null;
        else {
            for (ChessMove move : testBoard.getPiece(startPosition).pieceMoves(testBoard, startPosition)) {
                if (!suicide(move, testBoard)) finalizedMoves.add(move);
            }
        }
        return finalizedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException();
        } else {
            ChessBoard newBoard = this.board;
            ChessPiece pieceInQuestion = this.board.getPiece(move.getStartPosition());
            newBoard.removePiece(move.getStartPosition());
            newBoard.addPiece(move.getEndPosition(), pieceInQuestion);
            this.history.add(this.board);
            setBoard(newBoard);
        }
    }

    private ChessPosition findKing(TeamColor teamColor, ChessBoard testBoard) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                if (testBoard.getPiece(testPos).getPieceType() == ChessPiece.PieceType.KING
                        && testBoard.getPiece(testPos).getTeamColor() == teamColor) {
                    return testPos;
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
        ChessPosition kingPos = findKing(teamColor, this.board);
        ChessPosition enemyPos;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = board.getPiece(testPos);
                if (testPiece.getTeamColor() != teamColor) {
                    enemyPos = testPos;
                    if (validMoves(enemyPos).contains(kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPotentiallyInCheck(TeamColor teamColor, ChessBoard testBoard) {

        ChessPosition kingPos = findKing(teamColor, testBoard);
        ChessPosition enemyPos;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = testBoard.getPiece(testPos);
                if (testPiece.getTeamColor() != teamColor) {
                    enemyPos = testPos;
                    if (validHypotheticalMoves(enemyPos, testBoard).contains(kingPos)) {
                        return true;
                    }
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
        if (!isInCheck(teamColor)) return false;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                if (board.getPiece(testPos).getTeamColor() == teamColor && validMoves(testPos) != null) return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                if (board.getPiece(testPos).getTeamColor() == teamColor && validMoves(testPos) != null) return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
