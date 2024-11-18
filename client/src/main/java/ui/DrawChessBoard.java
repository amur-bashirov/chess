package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

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




    public static void draw(String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeader(out, color);
        out.println();
        if (color.equalsIgnoreCase("BLACK")) {
            String[][] board = drawBoardFromBlack(out);
            drawBoard(out,board,color);
        } else if(color.equalsIgnoreCase("WHITE")) {
            String[][] board = drawBoardFromWhite(out);
            drawBoard(out,board,color);
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

    static String[][] drawBoardFromBlack(PrintStream out) {
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
        return board;
    }

    static String[][] drawBoardFromWhite(PrintStream out) {
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
        return board;
    }

    static void drawBoard(PrintStream out, String[][] board, String color) {
        boolean isDarkSquare;
        for (int row = 0; row < board.length; row++) {
            out.print(SET_BG_COLOR_DARK_GREEN);
            out.print(SET_TEXT_COLOR_RED);
            if (color.equalsIgnoreCase("BLACK")) {
                out.print(1 + row + " " );
            } else if(color.equalsIgnoreCase("WHITE")) {
                out.print(8 - row + " " );
            }
            isDarkSquare = row % 2 == 0;

            for (int col = 0; col < board[row].length; col++) {
                if (isDarkSquare) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(board[row][col]);
                isDarkSquare = !isDarkSquare;
            }
            out.print(SET_BG_COLOR_DARK_GREEN);
            out.print(SET_TEXT_COLOR_RED);
            if (color.equalsIgnoreCase("BLACK")) {
                out.print(1 + row + " " );
            } else if(color.equalsIgnoreCase("WHITE")) {
                out.print(8 - row + " " );
            }
            out.print(SET_BG_COLOR_BLACK);
            out.println();
        }
    }
}
