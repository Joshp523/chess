package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private PieceType type;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(obj.getClass() != this.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece)obj;
        return this.getPieceType() == that.getPieceType() && this.getTeamColor() == that.getTeamColor();
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getPieceType(), this.getTeamColor());
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN,
        BLANK
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> possibilities;
        switch (type) {
            case KING:
                possibilities = MoveCalculator.kingMoves(board, myPosition);
                break;
            case QUEEN:
                possibilities = MoveCalculator.bishopMoves(board, myPosition);
                possibilities.addAll(MoveCalculator.rookMoves(board, myPosition));
                break;
            case BISHOP:
                possibilities = MoveCalculator.bishopMoves(board, myPosition);
                break;
            case KNIGHT:
                possibilities = MoveCalculator.knightMoves(board, myPosition);
                break;
            case ROOK:
                possibilities = MoveCalculator.rookMoves(board, myPosition);
                break;
            case PAWN:
                possibilities = MoveCalculator.pawnMoves(board, myPosition);
                break;
            default:
                possibilities = new ArrayList<>();
        }
        return possibilities;
    }
}
