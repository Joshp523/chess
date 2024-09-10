package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
    }
    @Override
    public boolean equals(java.lang.Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        ChessMove that = (ChessMove)obj;
        if(this.getStartPosition() != that.getStartPosition() || this.getEndPosition() != that.getEndPosition() || this.getPromotionPiece() != that.getPromotionPiece()) return false;
        return true;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getStartPosition(),this.getEndPosition(), this.getPromotionPiece());
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() { return startPosition; }


    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() { return endPosition;}

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return null;
    }
}
