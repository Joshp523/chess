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
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(getStartPosition(), chessMove.getStartPosition())
                && Objects.equals(getEndPosition(), chessMove.getEndPosition())
                && getPromotionPiece() == chessMove.getPromotionPiece();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }
//    @Override
//    public boolean equals(java.lang.Object obj) {
//        if(this == obj) return true;
//        if(obj == null) return false;
//        if(obj.getClass() != this.getClass()) return false;
//        ChessMove that = (ChessMove)obj;
//        if(this.getStartPosition().getRow() != that.getStartPosition().getRow()
//                ||this.getStartPosition().getColumn() != that.getStartPosition().getColumn()
//                || this.getEndPosition().getRow() != that.getEndPosition().getRow()
//                || this.getEndPosition().getColumn() != that.getEndPosition().getColumn()
//                || this.getPromotionPiece() != that.getPromotionPiece()) return false;
//        return true;
//    }
//
//    @Override
//    public int hashCode(){
//        return Objects.hash(this.getStartPosition(),this.getEndPosition(), this.getPromotionPiece());
//    }

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
        return promotionPiece;
    }
}
