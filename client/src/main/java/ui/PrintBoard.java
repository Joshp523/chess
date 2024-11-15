package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;


public class PrintBoard {

    public void main() {
        mainProtocol(0);
        mainProtocol(9);
    }

    private void mainProtocol(int black) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawHeaders(out);
        drawBoard(out, black);
        drawHeaders(out);
        setBlack(out);
        out.println();
    }

    private void drawBoard(PrintStream out, int black) {
        for (int row = 1; row <= 8; row++) {
            int label = abs(black - row);
            setText(out);
            out.print(" " + label + " ");
            drawRow(out, label);
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

    private void drawRow(PrintStream out, int row) {
        switch (row) {
                 case 1 -> WhiteRear(out);
            case 8 -> BlackRear(out);
            case 2 -> WhiteVan(out);
            case 7 -> BlackVan(out);
            case 6 -> whiteFirst(out);
            case 4 -> whiteFirst(out);
            default -> blackFirst(out);

        }
    }

    private void blackFirst(PrintStream out) {
        for (int row = 1; row <= 4; row++) {
            setBlack(out);
            out.print(EMPTY);
            setWhite(out);
            out.print(EMPTY);
        }
    }

    private void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }


    private void whiteFirst(PrintStream out) {
        for (int row = 1; row <= 4; row++) {
            setWhite(out);
            out.print(EMPTY);
            setBlack(out);
            out.print(EMPTY);
        }
    }

    private void BlackVan(PrintStream out) {
        for (int row = 1; row <= 4; row++) {
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(BLACK_PAWN);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(BLACK_PAWN);
        }
    }

    private void WhiteVan(PrintStream out) {
        for (int row = 1; row <= 4; row++) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(BLACK_PAWN);
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(BLACK_PAWN);
        }
    }

    private void BlackRear(PrintStream out) {
        for (int row = 1; row <= 4; row++) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(BLACK_ROOK);
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(BLACK_KNIGHT);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(BLACK_BISHOP);
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(BLACK_QUEEN);
        }
    }

    private void WhiteRear(PrintStream out) {
        for (int row = 1; row <= 4; row++) {
            setBlack(out);
            out.print(EMPTY);
            setWhite(out);
            out.print(EMPTY);
        }
    }

    void drawHeaders(PrintStream out) {
        setRed(out);
        String[] headers = {"A", "B", "C", "D", "E", "F", "G", "H"};
        out.print("    ");
        setText(out);
        for (String header : headers) {
            out.print(header + "  ");
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