package chess;

import java.util.AbstractList;
import java.util.ArrayList;

public class MoveCalculator {

    public static int validityChecker(ChessPosition probe, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //check if that test position is on the board
        if (probe.getRow() < 8 && probe.getRow() > 0 && probe.getColumn() < 8 && probe.getColumn() > 0) {
            //check that there are no teammates on that square
            if (board.getPiece(probe) == null) {
                //a return value of 1 means that the space is empty and a valid move
                return 1;
            } else if (board.getPiece(probe).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                //a return value of 2 means that the space is occupied by an opponent's piece, and is a valid move.
                return 2;
            }
        }
        //a return value of 3 means the space is out of bounds and invalid
        return 3;
    }

    public static ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int rowStep;
        int colStep;
        //iterate through every square within range
        for (rowStep = -1; rowStep <= 1; ++rowStep) {
            for (colStep = -1; colStep <= 1; ++colStep) {
                ChessPosition testPos = new ChessPosition(rowStep + myPosition.getRow(), colStep + myPosition.getColumn());
                if (validityChecker(testPos, board, myPosition) <= 2) {
                    //append this new move to the array of possible moves.
                    ChessMove newMove = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(newMove);
                }
            }
        }
        return possibleMoves;
    }

    public static ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //upper right diagonal
        int increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(increment + myPosition.getRow(), increment + myPosition.getColumn());
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        //upper left diagonal
        increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(increment + myPosition.getRow(), myPosition.getColumn() - increment);
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        //lower left diagonal
        increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() - increment, myPosition.getColumn() - increment);
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        //lower right diagonal
        increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() - increment, myPosition.getColumn() + increment);
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        return possibleMoves;
    }

    public static ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //to the right
        int increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow(), increment + myPosition.getColumn());
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        //to the left
        increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - increment);
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        //up
        increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() + increment, myPosition.getColumn());
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        //down
        increment = 1;
        while (increment < 8) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() - increment, myPosition.getColumn());
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        return possibleMoves;
    }

    public static ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int limit;
        if (myPosition.getRow() == 2) limit = 3;
        else limit = 2;
        int increment = 1;
        while (increment < limit) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() + increment, myPosition.getColumn());
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                ++increment;
            } else increment = 9;
        }

        ChessPosition leftAttack = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (validityChecker(leftAttack, board, myPosition) == 2) {
            //append this new move to the array of possible moves.
            ChessMove newMove = new ChessMove(myPosition, leftAttack, null);
            possibleMoves.add(newMove);
        }

        ChessPosition rightAttack = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (validityChecker(rightAttack, board, myPosition) == 2) {
            //append this new move to the array of possible moves.
            ChessMove newMove = new ChessMove(myPosition, rightAttack, null);
            possibleMoves.add(newMove);
        }
        if (myPosition.getRow() == 7) {

        }
        return possibleMoves;
    }
}



