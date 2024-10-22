package chess;

import java.util.ArrayList;

import static chess.ChessPiece.PieceType.QUEEN;
import static chess.ChessPiece.PieceType.BISHOP;
import static chess.ChessPiece.PieceType.KNIGHT;
import static chess.ChessPiece.PieceType.ROOK;

public class MoveCalculator {

    public static int validityChecker(ChessPosition probe, ChessBoard board, ChessPosition myPosition) {
        //check if that test position is on the board
        if (probe.getRow() <= 8 && probe.getRow() >= 1 && probe.getColumn() <= 8 && probe.getColumn() >= 1) {
            //check that there are no teammates on that square
            if (board.getPiece(probe) == null) {
                //a return value of 1 means that the space is empty and a valid move
                return 1;
            } else if (board.getPiece(probe).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                //a return value of 2 means that the space is occupied by an opponent's piece, and is a valid move.
                return 2;
            }
            //a return value of 4 means that the spot is occupied by a teammate and therefore invalid
            else return 4;
        }
        //a return value of 3 means the space is out of bounds and therefore invalid
        return 3;
    }

    private static ChessMove spotMove(int rowStep, int colStep, ChessPosition myPosition, ChessBoard board) {
        ChessPosition testPos = new ChessPosition(myPosition.getRow() + rowStep, myPosition.getColumn() + colStep);
        if (validityChecker(testPos, board, myPosition) <= 2) {
            ChessMove newMove = new ChessMove(myPosition, testPos, null);
            return newMove;
        }
        return null;
    }

    public static ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int rowStep;
        int colStep;
        //iterate through every square within range
        for (rowStep = -1; rowStep <= 1; ++rowStep) {
            for (colStep = -1; colStep <= 1; ++colStep) {
                ChessMove newMove = spotMove(rowStep, colStep, myPosition, board);
                if (newMove != null) {
                    possibleMoves.add(newMove);
                }
            }
        }
        return possibleMoves;
    }

    public static ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int smallStep;
        int bigStep;
        //iterate through every square within range
        for (smallStep = -1; smallStep <= 1; smallStep += 2) {
            for (bigStep = -2; bigStep <= 2; bigStep += 4) {
                ChessMove newMove = spotMove(bigStep, smallStep, myPosition, board);
                if (newMove != null) {
                    possibleMoves.add(newMove);
                }
                ChessMove nextMove = spotMove(smallStep, bigStep, myPosition, board);
                if (nextMove != null) {
                    possibleMoves.add(nextMove);
                }
            }
        }
        return possibleMoves;
    }

    private static ArrayList<ChessMove> straightShot(int rowSign, int columnSign, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> shot = new ArrayList<>();
        int increment = 1;
        while (increment < 9) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() + (rowSign * increment), myPosition.getColumn() + (columnSign * increment));
            if (validityChecker(testPos, board, myPosition) == 1) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                shot.add(newMove);
                ++increment;
                continue;
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                shot.add(newMove);
                return shot;
            } else return shot;
        }
        return shot;
    }

    public static ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //upper right diagonal
        possibleMoves.addAll(straightShot(1, 1, board, myPosition));
        //upper left diagonal
        possibleMoves.addAll(straightShot(1, -1, board, myPosition));
        //lower left diagonal
        possibleMoves.addAll(straightShot(-1, -1, board, myPosition));
        //lower right diagonal
        possibleMoves.addAll(straightShot(-1, 1, board, myPosition));
        return possibleMoves;
    }

    public static ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //right
        possibleMoves.addAll(straightShot(0, 1, board, myPosition));
        //left
        possibleMoves.addAll(straightShot(0, -1, board, myPosition));
        //up
        possibleMoves.addAll(straightShot(1, 0, board, myPosition));
        //down
        possibleMoves.addAll(straightShot(-1, 0, board, myPosition));
        return possibleMoves;
    }


    public static ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int direction;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else direction = -1;

        int limit;
        if (myPosition.getRow() == 2 || myPosition.getRow() == 7) limit = 3;
        else limit = 2;
        int increment = 1;
        while (increment < limit) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() + increment * direction, myPosition.getColumn());
            if (validityChecker(testPos, board, myPosition) == 1) {
                if ((direction == 1 && myPosition.getRow() == 7) || (direction == -1 && myPosition.getRow() == 2)) {
                    ChessPiece.PieceType pieces[] = {QUEEN, BISHOP, KNIGHT, ROOK};
                    for (ChessPiece.PieceType promotion : pieces) {
                        ChessMove promoMove = new ChessMove(myPosition, testPos, promotion);
                        possibleMoves.add(promoMove);
                    }
                } else {
                    ChessMove newMove = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(newMove);
                }
                ++increment;
            } else increment = 9;
        }
        for (int a = -1; a <= 1; a += 2) {
            ChessPosition attack = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + a);
            if (validityChecker(attack, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                if ((direction == 1 && myPosition.getRow() == 7) || (direction == -1 && myPosition.getRow() == 2)) {
                    ChessPiece.PieceType pieces[] = {QUEEN, BISHOP, KNIGHT, ROOK};
                    for (ChessPiece.PieceType promotion : pieces) {
                        ChessMove promoMove = new ChessMove(myPosition, attack, promotion);
                        possibleMoves.add(promoMove);
                    }
                } else {
                    ChessMove newMove = new ChessMove(myPosition, attack, null);
                    possibleMoves.add(newMove);
                }
            }
        }
        return possibleMoves;
    }
}



