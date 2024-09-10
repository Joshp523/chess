package chess;

import java.util.AbstractList;
import java.util.ArrayList;

import static chess.ChessPiece.PieceType.QUEEN;
import static chess.ChessPiece.PieceType.BISHOP;
import static chess.ChessPiece.PieceType.KNIGHT;
import static chess.ChessPiece.PieceType.ROOK;

public class MoveCalculator {

    public static int validityChecker(ChessPosition probe, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //check if that test position is on the board
        if (probe.getRow() < 9 && probe.getRow() > 0 && probe.getColumn() < 9 && probe.getColumn() > 0) {
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
        System.out.println("Original Row: " + myPosition.getRow() + "Original Column: " + myPosition.getColumn());
        System.out.println();
        //upper right diagonal
        int increment = 1;
        while (increment < 9) {
            ChessPosition testPos = new ChessPosition(increment + myPosition.getRow(), increment + myPosition.getColumn());
            System.out.println("1");
            System.out.println("New Row: " + testPos.getRow() + " New Col: " + testPos.getColumn());
            System.out.println();
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
        while (increment < 9) {
            ChessPosition testPos = new ChessPosition(increment + myPosition.getRow(), myPosition.getColumn() - increment);
            System.out.println("2");
            System.out.println("New Row: " + testPos.getRow() + " New Col: " + testPos.getColumn());
            System.out.println();
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
        while (increment < 9) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() - increment, myPosition.getColumn() - increment);
            System.out.println("3");
            System.out.println("New Row: " + testPos.getRow() + " New Col: " + testPos.getColumn());
            System.out.println();
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
        while (increment < 9) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow() - increment, myPosition.getColumn() + increment);
            System.out.println("4");
            System.out.println("New Row: " + testPos.getRow() + " New Col: " + testPos.getColumn());
            System.out.println();
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

        System.out.println("Actual Moves");
        for (ChessMove move : possibleMoves) {
            System.out.println("New Row: " + move.getEndPosition().getRow() + " \nNew Col: " + move.getEndPosition().getColumn());
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
            } else if (validityChecker(testPos, board, myPosition) == 2) {
                //append this new move to the array of possible moves.
                ChessMove newMove = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(newMove);
                increment = 9;
            } else increment = 9;
        }
        System.out.print(possibleMoves);
        return possibleMoves;
    }

    public static ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int smallStep;
        int bigStep;
        //iterate through every square within range
        for (smallStep = -1; smallStep <= 1; smallStep += 2) {
            for (bigStep = -2; bigStep <= 2; bigStep += 4) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow() + smallStep, myPosition.getColumn() + bigStep);
                if (validityChecker(testPos, board, myPosition) <= 2) {
                    //append this new move to the array of possible moves.
                    ChessMove newMove = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(newMove);
                }
                ChessPosition secondPos = new ChessPosition(myPosition.getRow() + bigStep, myPosition.getColumn() + smallStep);
                if (validityChecker(testPos, board, myPosition) <= 2) {
                    //append this new move to the array of possible moves.
                    ChessMove newMove = new ChessMove(myPosition, secondPos, null);
                    possibleMoves.add(newMove);
                }
            }
        }
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
                //append this new move to the array of possible moves.
                if ((direction == 1 && myPosition.getRow() == 7) || (direction == -1 && myPosition.getRow() == 2)) {
                    ChessPiece.PieceType pieces[] = {QUEEN, BISHOP, KNIGHT, ROOK};
                    for (ChessPiece.PieceType promotion : pieces) {
                        ChessMove promoMove = new ChessMove(myPosition, testPos, promotion);
                        possibleMoves.add(promoMove);
                    }
                }else {
                    ChessMove newMove = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(newMove);
                }
                ++increment;
            } else increment = 9;
        }

        ChessPosition leftAttack = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() - 1);
        if (validityChecker(leftAttack, board, myPosition) == 2) {
            //append this new move to the array of possible moves.
            if ((direction == 1 && myPosition.getRow() == 7) || (direction == -1 && myPosition.getRow() == 2)) {
                ChessPiece.PieceType pieces[] = {QUEEN, BISHOP, KNIGHT, ROOK};
                for (ChessPiece.PieceType promotion : pieces) {
                    ChessMove promoMove = new ChessMove(myPosition, leftAttack, promotion);
                    possibleMoves.add(promoMove);
                }
            }else {
                ChessMove newMove = new ChessMove(myPosition, leftAttack, null);
                possibleMoves.add(newMove);
            }
        }

        ChessPosition rightAttack = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + 1);
        if (validityChecker(rightAttack, board, myPosition) == 2) {
            //append this new move to the array of possible moves.
            if ((direction == 1 && myPosition.getRow() == 7) || (direction == -1 && myPosition.getRow() == 2)) {
                ChessPiece.PieceType pieces[] = {QUEEN, BISHOP, KNIGHT, ROOK};
                for (ChessPiece.PieceType promotion : pieces) {
                    ChessMove promoMove = new ChessMove(myPosition, rightAttack, promotion);
                    possibleMoves.add(promoMove);
                }

            }else {
                ChessMove newMove = new ChessMove(myPosition, rightAttack, null);
                possibleMoves.add(newMove);
            }
        }

        System.out.println("Actual Moves");
        for (ChessMove move : possibleMoves) {
            System.out.println("New Row: " + move.getEndPosition().getRow() + " \nNew Col: " + move.getEndPosition().getColumn());
        }
        return possibleMoves;
    }
}



