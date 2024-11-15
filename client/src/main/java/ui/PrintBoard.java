package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;


public class PrintBoard {
    private final String bb = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_DARK_GREY;
    private final String ww = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_LIGHT_GREY;
    private final String b = SET_TEXT_COLOR_BLACK;
    private final String w = SET_TEXT_COLOR_WHITE;
    private final String p = BLACK_PAWN;
    private final String r = BLACK_ROOK;
    private final String s = BLACK_BISHOP;
    private final String n = BLACK_KNIGHT;
    private final String q = BLACK_QUEEN;
    private final String k = BLACK_KING;


    public void main() {
        mainProtocol(0);
        mainProtocol(9);
    }

    private void mainProtocol(int black) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawHeaders(out, black);
        drawBoard(out, black);
        drawHeaders(out, black);
        setBlack(out);
        out.println();
    }

    private void drawBoard(PrintStream out, int black) {
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

    private void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
    }

    private void drawRow(PrintStream out, int row, int black) {
        switch (row) {
            case 1 -> Rear(w, out, black);
            case 8 -> Rear(b, out, abs(black-9));
            case 2 -> Van(w,out,black);
            case 7 -> Van(b,out,abs(black-9));
            case 4 -> blackFirst(out, black);
            case 6 -> blackFirst(out, black);
            default -> whiteFirst(out, black);

        }
    }

    private void blackFirst(PrintStream out, int toggle) {
        if(toggle == 9){
            whiteFirst(out, 0);
        }else{
        for (int row = 1; row <= 4; row++) {
            populateSquare(bb, bb, p, out);
            populateSquare(ww, ww, p, out);
        }}
    }


    private void whiteFirst(PrintStream out, int toggle) {
        if(toggle == 9){
            blackFirst(out, 0);
        }else{
        for (int row = 1; row <= 4; row++) {
            populateSquare(ww, ww, p, out);
            populateSquare(bb, bb, p, out);
        }}
    }


    private void Van(String team, PrintStream out, int black) {
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

    private void Rear(String team, PrintStream out, int black) {
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


    void drawHeaders(PrintStream out, int black) {
        setText(out);
        if (black == 9) {
            out.print("    A   B  C   D   E  F   G   H" + EMPTY);
        } else {
            out.print("    H   G  F   E   D  C   B   A" + EMPTY);
        }
        setBlack(out);
        out.println();
    }

    void setText(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    void populateSquare(String squareColor, String pieceColor, String piece, PrintStream out) {
        out.print(squareColor+pieceColor+piece);
    }

}