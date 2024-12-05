package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private final String color;

    private static String[] Row1 ={};
            //{BLACK_ROOK, BLACK_KNIGHT,
            //BLACK_BISHOP,BLACK_KING,BLACK_QUEEN, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    private static final List<String> Row2 = new ArrayList<>();
//                    {BLACK_ROOK, BLACK_KNIGHT,
//            BLACK_BISHOP,BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    private static final String[] Row3 = {BLACK_PAWN, BLACK_PAWN,
            BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN};
    private static final String[] Row4 = {WHITE_PAWN, WHITE_PAWN,
            WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN};
    private static final String[] Row5 = {WHITE_ROOK, WHITE_KNIGHT,
            WHITE_BISHOP, WHITE_KING,WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
    private static final String[] Row6 = {WHITE_ROOK, WHITE_KNIGHT,
            WHITE_BISHOP,WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
    private static final String[] Row7 = {EMPTY, EMPTY,
            EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};

    private static final String SPACE = " ";

    public DrawChessBoard(String color){
        this.color = color;
    }

    public static void printCurrentBoard(ChessBoard board) {
        var squares = board.getSquares();
        List<String> Row1 = new ArrayList<>();
        List<String> Row3 = new ArrayList<>();
        List<String> Row4 = new ArrayList<>();
        List<String> Row5 = new ArrayList<>();
        List<String> Row6 = new ArrayList<>();
        List<String> Row7 = new ArrayList<>();
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                ChessPiece piece = squares[row][col];
                if (piece != null) {
                    System.out.print(piece.getTeamColor() + " " + piece.getPieceType() + "\t");
                    String piece2 = EMPTY;
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        switch (piece.getPieceType()) {
                            case BISHOP: {
                                piece2 = WHITE_BISHOP;
                                break;
                            }
                            case PAWN: {
                                piece2 = WHITE_PAWN;
                                break;
                            }
                            case KING: {
                                piece2 = WHITE_KING;
                                break;
                            }
                            case QUEEN: {
                                piece2 = WHITE_QUEEN;
                                break;
                            }
                            case ROOK: {
                                piece2 = WHITE_ROOK;
                                break;
                            }
                            case KNIGHT: {
                                piece2 = WHITE_KNIGHT;
                                break;
                            }
                        }
                    }
                    if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        switch (piece.getPieceType()) {
                            case BISHOP: {
                                piece2 = BLACK_BISHOP;
                                break;
                            }
                            case PAWN: {
                                piece2 = BLACK_PAWN;
                                break;
                            }
                            case KING: {
                                piece2 = BLACK_KING;
                                break;
                            }
                            case QUEEN: {
                                piece2 = BLACK_KING;
                                break;
                            }
                            case ROOK: {
                                piece2 = BLACK_ROOK;
                                break;
                            }
                            case KNIGHT: {
                                piece2 = BLACK_KNIGHT;
                                break;
                            }
                        }
                    }
                    switch (row){
                        case 0:{
                            Row1.add(piece2);
                            break;
                        }
                        case 1:{
                            Row2.add(piece2);
                            break;
                        }
                        case 2:{
                            Row3.add(piece2);
                            break;
                        }case 3:{
                            Row4.add(piece2);
                            break;
                        }case 4:{
                            Row5.add(piece2);
                            break;
                        }
                        case 5:{
                            Row6.add(piece2);
                            break;
                        }case 6:{
                            Row7.add(piece2);
                            break;
                        }


                    }
                } else {
                    System.out.print("EMPTY\t");
                }
            }
            System.out.println();
        }
    }





    public static void main(String [] args) {
        ChessGame game = new ChessGame();
        ChessBoard board2 = game.getBoard();
        System.out.println(new Gson().toJson(board2));
        printCurrentBoard(board2);
        String color = "white";
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeader(out, color);
        out.println();
        if (color.equalsIgnoreCase("BLACK")) {
            String[][] board = drawBoardFromBlack(out);
            drawBoard(out,board,color, Row2);
        } else if(color.equalsIgnoreCase("WHITE")) {
            String[][] board = drawBoardFromWhite(out);
            drawBoard(out,board,color, Row2);
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
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    static String[][] drawBoardFromBlack(PrintStream out) {
        String[][] board = {
                Row1,
                Row3,
                Row7,
                Row7,
                Row7,
                Row7,
                Row4,
                Row5
        };
        out.print(SET_TEXT_COLOR_BLUE);
        return board;
    }

    static String[][] drawBoardFromWhite(PrintStream out) {
        String[] row2Array = Row2.toArray(new String[0]);
        String[][] board = {
                Row6,
                Row4,
                Row7,
                Row7,
                Row7,
                Row7,
                Row3,
                row2Array
        };
        return board;
    }

    static void drawBoard(PrintStream out, String[][] board, String color, List<String> row2) {
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
                    out.print(SET_BG_COLOR_DARK_GREY);
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
            out.print(SET_BG_COLOR_DARK_GREY);
            out.println();
        }
    }
}
