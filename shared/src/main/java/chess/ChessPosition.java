package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    @Override
    public boolean equals(java.lang.Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        ChessPosition that = (ChessPosition)obj;
        if(this.getRow() != that.getRow() || this.getColumn() != that.getColumn()) return false;
        return true;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getRow(),this.getColumn());
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}
