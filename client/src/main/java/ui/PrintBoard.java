package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;


public class PrintBoard {
    static final String bb = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_DARK_GREY;
    static final String ww = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_LIGHT_GREY;
    static final String b = SET_TEXT_COLOR_BLACK;
    static final String w = SET_TEXT_COLOR_WHITE;
    static final String p = BLACK_PAWN;
    static final String r = BLACK_ROOK;
    static final String s = BLACK_BISHOP;
    static final String n = BLACK_KNIGHT;
    static final String q = BLACK_QUEEN;
    static final String k = BLACK_KING;


    public static String main(ChessBoard board, String perspective) {
        switch (perspective){
            case b: return mainProtocol(0);
            default: return mainProtocol(9);
        }
    }

    static String mainProtocol(int black) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawHeaders(out, black);
        drawBoard(out, black);
        drawHeaders(out, black);
        setBlack(out);
        out.println();
        return out.toString();
    }

    static void drawBoard(PrintStream out, int black) {
        for (int row = 1; row <= 8; row++) {
            int label = abs(black - row);
            setText(out);
            out.print(" " + label + " ");
            drawRow(out, label, black);
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

    static void drawRow(PrintStream out, int row, int black) {
        switch (row) {
            case 1 -> rear(w, out, black);
            case 8 -> rear(b, out, abs(black-9));
            case 2 -> vanguard(w,out,black);
            case 7 -> vanguard(b,out,abs(black-9));
            case 4 -> blackFirst(out, black);
            case 6 -> blackFirst(out, black);
            default -> whiteFirst(out, black);

        }
    }

    static void blackFirst(PrintStream out, int toggle) {
        if(toggle == 9){
            whiteFirst(out, 0);
        }else{
        for (int row = 1; row <= 4; row++) {
            populateSquare(bb, bb, p, out);
            populateSquare(ww, ww, p, out);
        }}
    }


    static void whiteFirst(PrintStream out, int toggle) {
        if(toggle == 9){
            blackFirst(out, 0);
        }else{
        for (int row = 1; row <= 4; row++) {
            populateSquare(ww, ww, p, out);
            populateSquare(bb, bb, p, out);
        }}
    }


    static void vanguard(String team, PrintStream out, int black) {
        if (black == 9) {
            for (int row = 1; row <= 4; row++) {
                populateSquare(ww, team, p, out);
                populateSquare(bb, team, p, out);
            }
        } else {
            for (int row = 1; row <= 4; row++) {
                populateSquare(bb, team, p, out);
                populateSquare(ww, team, p, out);
            }
        }
    }

    static void rear(String team, PrintStream out, int black) {
        String left = ww;
        String right = bb;
        String one = k;
        String two = q;
        if (black == 9) {
            left = bb;
            right = ww;
            one = q;
            two = k;
        }
        if (team == b){
            String three = "";
            three = one;
            one = two;
            two = three;
        }

        populateSquare(left, team, r, out);
        populateSquare(right, team, n, out);
        populateSquare(left, team, s, out);
        populateSquare(right, team, one, out);
        populateSquare(left, team, two, out);
        populateSquare(right, team, s, out);
        populateSquare(left, team, n, out);
        populateSquare(right, team, r, out);
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

    static void populateSquare(String squareColor, String pieceColor, String piece, PrintStream out) {
        out.print(squareColor+pieceColor+piece);
    }

}