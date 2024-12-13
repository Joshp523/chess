package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static chess.ChessGame.TeamColor.GREEN;
import static chess.ChessPiece.PieceType.BLANK;
import static java.lang.Math.abs;
import static ui.EscapeSequences.*;


public class PrintBoard {
    static final String BLACKBACK = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_DARK_GREY;
    static final String WHITEBACK = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_LIGHT_GREY;
    static final String DARKGREEN = SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_DARK_GREEN;
    static final String LIGHTGREEN = SET_BG_COLOR_GREEN + SET_TEXT_COLOR_GREEN;
    static final String BLACKTEXT = SET_TEXT_COLOR_BLACK;
    static final String WHITETEXT = SET_TEXT_COLOR_WHITE;
    static final String PAWN = BLACK_PAWN;
    static final String ROOK = BLACK_ROOK;
    static final String BISHOP = BLACK_BISHOP;
    static final String KNIGHT = BLACK_KNIGHT;

    static final String QUEEN = BLACK_QUEEN;
    static final String KING = BLACK_KING;


    public static String main(AnnotatedChessBoard board, ChessGame.TeamColor teamColor) {
        switch (teamColor) {
            case WHITE -> mainProtocol(board, 9);
            case BLACK -> mainProtocol(board, 0);
            default -> mainProtocol(board, 9);
        }
        return null;
    }


    static String mainProtocol(AnnotatedChessBoard board, int black) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        out.println();
        drawHeaders(out, black);
        drawBoard(out, board, black);
        drawHeaders(out, black);
        setBlack(out);
        out.println();
        return out.toString();
    }

    static void drawBoard(PrintStream out, AnnotatedChessBoard board, int black) {
        for (int row = 1; row <= 8; row++) {
            int label = abs(black - row);
            setText(out);
            out.print(" " + label + " ");
            drawRow(out, label, black, board);
            setText(out);
            out.print(" " + label + " ");
            setBlack(out);
            out.println();
        }
    }

    static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
    }

    static void drawRow(PrintStream out, int row, int black, AnnotatedChessBoard board) {
        if (black == 0) {
            switch (row) {
                case 1 -> whiteFirst(row, board, 1, out);
                case 2 -> blackFirst(row, board, 1, out);
                case 3 -> whiteFirst(row, board, 1, out);
                case 4 -> blackFirst(row, board, 1, out);
                case 5 -> whiteFirst(row, board, 1, out);
                case 6 -> blackFirst(row, board, 1, out);
                case 7 -> whiteFirst(row, board, 1, out);
                case 8 -> blackFirst(row, board, 1, out);
            }
        } else {
            switch (row) {
                case 8 -> whiteFirst(row, board, -1, out);
                case 7 -> blackFirst(row, board, -1, out);
                case 6 -> whiteFirst(row, board, -1, out);
                case 5 -> blackFirst(row, board, -1, out);
                case 4 -> whiteFirst(row, board, -1, out);
                case 3 -> blackFirst(row, board, -1, out);
                case 2 -> whiteFirst(row, board, -1, out);
                case 1 -> blackFirst(row, board, -1, out);
            }
        }
    }

    static void whiteFirst(int row, AnnotatedChessBoard board, int toggle, PrintStream out) {
        if (toggle == -1) {
            for (int col = 1; col <= 8; col += 2) {
                populateRow(row, board, col, 1, WHITEBACK, BLACKBACK, out, LIGHTGREEN, DARKGREEN);
            }
        } else {
            for (int col = 8; col > 0; col -= 2) {
                populateRow(row, board, col, -1, WHITEBACK, BLACKBACK, out, LIGHTGREEN, DARKGREEN);
            }
        }
    }

    private static void populateRow(int row,
                                    AnnotatedChessBoard board,
                                    int col,
                                    int increment,
                                    String background1,
                                    String background2,
                                    PrintStream out,
                                    String background1v,
                                    String background2v) {
        ChessPosition pos1 = new ChessPosition(row, col);
        if (board.getValidity(pos1)) {
            background1 = background1v;
        }
        ChessPiece piece1 = board.getPiece(pos1);
        if (piece1 == null) {
            populateSquare(background1, GREEN, BLANK, out);
        } else {
            ChessGame.TeamColor color = piece1.getTeamColor();
            ChessPiece.PieceType pieceKind = piece1.getPieceType();
            populateSquare(background1, color, pieceKind, out);
        }
        ChessPosition pos2 = new ChessPosition(row, col + increment);
            if (board.getValidity(pos2)) {
                background2 = background2v;
            }
        ChessPiece piece2 = board.getPiece(pos2);
        if (piece2 == null) {
            populateSquare(background2, GREEN, BLANK, out);
        } else {
            ChessGame.TeamColor color2 = piece2.getTeamColor();
            ChessPiece.PieceType pieceKind2 = piece2.getPieceType();
            populateSquare(background2, color2, pieceKind2, out);
        }
    }

    static void blackFirst(int row, AnnotatedChessBoard board, int toggle, PrintStream out) {
        if (toggle == -1) {
            for (int col = 1; col <= 8; col += 2) {
                populateRow(row, board, col, 1, BLACKBACK, WHITEBACK, out, DARKGREEN, LIGHTGREEN);
            }
        } else {
            for (int col = 8; col > 0; col -= 2) {
                populateRow(row, board, col, -1, BLACKBACK, WHITEBACK, out, DARKGREEN, LIGHTGREEN);
            }
        }
    }

    static void drawHeaders(PrintStream out, int black) {
        setText(out);
        if (black == 9) {
            out.print("    A   B  C   D   E  F   G   H" + EMPTY);
        } else {
            out.print("    H   G  F   E   D  C   B   A" + EMPTY);
        }
        setBlack(out);
        out.println();
    }

    static void setText(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    static void populateSquare(String squareColor, ChessGame.TeamColor color, ChessPiece.PieceType type, PrintStream out) {

        String c;
        String t;
        switch (color) {
            case WHITE -> c = WHITETEXT;
            case BLACK -> c = BLACKTEXT;
            default -> c = squareColor;
        }
        switch (type) {
            case PAWN -> t = PAWN;
            case KNIGHT -> t = KNIGHT;
            case QUEEN -> t = QUEEN;
            case KING -> t = KING;
            case BISHOP -> t = BISHOP;
            case ROOK -> t = ROOK;
            default -> t = PAWN;
        }

        out.print(squareColor + c + t);
    }

}