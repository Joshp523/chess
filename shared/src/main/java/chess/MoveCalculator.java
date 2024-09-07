package chess;

import java.util.AbstractList;
import java.util.ArrayList;

public class MoveCalculator {

    public static ArrayList<ChessMove> validityChecker(ArrayList<ChessPosition> draft, ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        for (ChessPosition testPos : draft) {
            //check if that test position is on the board
            if (testPos.getRow() < 9 && testPos.getRow() > 0 && testPos.getColumn() < 9 && testPos.getColumn() > 0) {
                //check that there are no teammates on that square
                if (board.getPiece(testPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    //append this new move to the array of possible moves.
                    ChessMove newMove = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(newMove);
                }
            }
        }
        return possibleMoves;
    }

    public static ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessPosition> draft = new ArrayList<>();
        int rowStep;
        int colStep;
        //iterate through every square within range
        for (rowStep = -1; rowStep <= 1; ++rowStep) {
            for (colStep = -1; colStep <= 1; ++colStep) {
                ChessPosition testPos = new ChessPosition(rowStep + myPosition.getRow(), colStep + myPosition.getColumn());
                draft.add(testPos);
            }
        }
        return validityChecker(draft, board, myPosition);
    }

    public static ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessPosition> draft = new ArrayList<>();
        int increment;
        for (increment = -8; increment < 9; increment++) {
            ChessPosition testPos = new ChessPosition(increment + myPosition.getRow(), increment + myPosition.getColumn());
            draft.add(testPos);
            ChessPosition testPos2 = new ChessPosition(myPosition.getRow() - increment, increment + myPosition.getColumn());
            draft.add(testPos2);
        }
        return validityChecker(draft, board, myPosition);
    }

}



