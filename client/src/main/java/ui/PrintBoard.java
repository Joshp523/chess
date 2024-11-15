package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class PrintBoard {

    public void main() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawHeaders(out);
        drawBoard(out);
        drawHeaders(out);
    }

    private void drawBoard(PrintStream out) {
        for(int row = 1; row <= 8; row++) {
            setText(out);
            out.print(" " + row+ " ");
            drawRow(out,row);
            out.print(" " + row + " ");
            setBlack(out);
            out.println();
        }
    }

    private void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);

    }

    private void drawRow(PrintStream out, int row) {
        switch(row%2) {
//            case 1 -> WhiteRear(out);
//            case 8 -> BlackRear(out);
//            case 2 -> WhiteVan(out);
//            case 7 -> BlackVan(out);
            case 1 -> whiteFirst(out);
            default -> blackFirst(out);

        }
    }

    private void blackFirst(PrintStream out) {
        for(int row = 1; row <= 4; row++) {
            setBlack(out);
            out.print(EMPTY+ " ");
            setWhite(out);
            out.print(EMPTY+ " ");
        }
    }

    private void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private void whiteFirst(PrintStream out) {
        for(int row = 1; row <= 4; row++) {
            setWhite(out);
            out.print(EMPTY+ " ");
            setBlack(out);
            out.print(EMPTY+ " ");
        }
    }

    private void BlackVan(PrintStream out) {
    }

    private void WhiteVan(PrintStream out) {
    }

    private void BlackRear(PrintStream out) {
    }

    private void WhiteRear(PrintStream out) {
    }

    void drawHeaders(PrintStream out) {
        setRed(out);
        String[] headers = {"A", "B", "C", "D", "E", "F", "G", "H"};
        out.print("    ");
        setText(out);
        for (String header : headers) {
            out.print(header + "    ");
        }
        out.print("  ");
        setBlack(out);
        out.println();
    }

    void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    void setText(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}