package chess;

import java.util.Collection;

public class AnnotatedChessBoard extends ChessBoard {
    private boolean[][] possibilities = new boolean[8][8];
    ChessBoard board;

    public AnnotatedChessBoard(ChessBoard board, Collection<ChessMove> moves) {
        this.board = board;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                possibilities[i][j] = false;
            }
        }
        if (moves != null) {
            for (ChessMove move : moves) {
                addGreen(move.getEndPosition());
            }
        }

    }

    private void addGreen(ChessPosition position) {
        possibilities[position.getRow() - 1][position.getColumn() - 1] = true;
    }

    public boolean getValidity(ChessPosition position) {
        return possibilities[position.getRow() - 1][position.getColumn() - 1];
    }

    public ChessPiece getPiece(ChessPosition position) {
        return board.getPiece(position);
    }
}
