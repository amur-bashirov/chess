package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;
import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private final String color;

    private static final String[] BLACK_BACK_ROW = {BLACK_ROOK, BLACK_KNIGHT,
            BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    private static final String[] BLACK_PAWN_ROW = {BLACK_PAWN, BLACK_PAWN,
            BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN};
    private static final String[] WHITE_PAWN_ROW = {WHITE_PAWN, WHITE_PAWN,
            WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN};
    private static final String[] WHITE_BACK_ROW = {WHITE_ROOK, WHITE_KNIGHT,
            WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
    private static final String[] EMPTY_ROW = {EMPTY, EMPTY,
            EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};

    private static final String SPACE = " ";

    public DrawChessBoard(String color){
        this.color = color;
    }




    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);  // Clear the screen
        String color = "white";   // Color choice for the header text
        drawHeader(out, color);
        out.println();
        if (color.equalsIgnoreCase("BLACK")) {
            drawBoardFromBlack(out);
        } else{
            drawBoardFromWhite(out);
        }
        drawHeader(out, color);
    }

    static void drawHeader(PrintStream out,String color) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_RED);
        String[]letters = {};
        if (color.equalsIgnoreCase("BLACK")) {
            letters = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        } else if(color.equalsIgnoreCase("WHITE")) {
            letters = new String[]{"a","b","c","d","e","f","g","h"};
        }
        out.print("   ");

        for (String letter : letters) {
            out.print(letter + "\u2003 ");
        }
        out.print(" ");
        out.print(SET_BG_COLOR_BLACK);
    }

    static void drawBoardFromBlack(PrintStream out) {
        // Order rows from Black's perspective
        String[][] board = {
                BLACK_BACK_ROW,
                BLACK_PAWN_ROW,
                EMPTY_ROW,
                EMPTY_ROW,
                EMPTY_ROW,
                EMPTY_ROW,
                WHITE_PAWN_ROW,
                WHITE_BACK_ROW
        };
        out.print(SET_TEXT_COLOR_BLUE);
        drawBoard(out, board);
    }

    static void drawBoardFromWhite(PrintStream out) {
        // Order rows from White's perspective
        String[][] board = {
                WHITE_BACK_ROW,
                WHITE_PAWN_ROW,
                EMPTY_ROW,
                EMPTY_ROW,
                EMPTY_ROW,
                EMPTY_ROW,
                BLACK_PAWN_ROW,
                BLACK_BACK_ROW
        };
        out.print(SET_TEXT_COLOR_YELLOW);
        drawBoard(out, board);
    }

    static void drawBoard(PrintStream out, String[][] board) {
        boolean isDarkSquare;
        for (int row = 0; row < board.length; row++) {
            out.print(SET_BG_COLOR_DARK_GREEN);
            out.print(SET_TEXT_COLOR_RED);
            out.print(8 - row + " " );
            // Print row number from the perspective at the start of each row
            isDarkSquare = row % 2 == 0;

            for (int col = 0; col < board[row].length; col++) {
                if (isDarkSquare) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(board[row][col]);
                isDarkSquare = !isDarkSquare;  // Alternate square color
            }
            out.print(SET_BG_COLOR_DARK_GREEN);
            out.print(SET_TEXT_COLOR_RED);
            out.print((8 - row + " "));
            // Print row number from the perspective at the start of each row
            out.print(SET_BG_COLOR_BLACK);
            out.println();
        }
    }


}
