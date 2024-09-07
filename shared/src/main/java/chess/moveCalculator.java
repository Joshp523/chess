package chess;

import java.util.ArrayList;

public interface moveCalculator {
    ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition);
}

