package chess;

import java.util.ArrayList;

public class kingCalc implements moveCalculator {
    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int rowStep;
        int colStep;
        for(rowStep=-1; rowStep <=1; ++rowStep){
            for(colStep=-1; colStep <=1; ++colStep){
                ChessPosition testPos = new ChessPosition(rowStep + myPosition.getRow(), colStep+myPosition.getColumn());
                if (ChessBoard.getPiece(testPos).getTeamColor() != ChessBoard.getPiece(myPosition).getTeamColor()){
                    //append this new move to the array of possible moves.
                    ChessMove newMove = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(newMove);
                }
            }
        }
        return possibleMoves;
    }
}
