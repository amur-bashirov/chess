package ui;
import chess.*;
import com.google.gson.Gson;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private final String color;

    private static  List<String> Row1 = new ArrayList<>();
    private static  List<String> Row2 = new ArrayList<>();
    private static List<String> Row3 = new ArrayList<>();
    private static List<String> Row4 = new ArrayList<>();
    private static List<String> Row5 = new ArrayList<>();
    private static List<String> Row6 = new ArrayList<>();
    private static List<String> Row7 = new ArrayList<>();
    private static List<String> Row8 = new ArrayList<>();

    private static final String SPACE = " ";

    public DrawChessBoard(String color){
        this.color = color;
    }

    public static void printCurrentBoard(ChessBoard board) {
        var squares = board.getSquares();
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                ChessPiece piece = squares[row][col];
                String piece2 = EMPTY;
                if (piece != null) {
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
                                piece2 = BLACK_QUEEN;
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
                }else{
                    piece2 = EMPTY;
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
                        } case 7:{
                            Row8.add(piece2);
                        }


                    }

            }
            System.out.println();
        }
    }





    public static void draw(ChessGame game, ChessPosition position, String color) {
        ChessBoard board2 = game.getBoard();
        Collection<ChessMove> moves = null;
        if (position !=null) {
            moves = game.validMoves(position);
        }
        printCurrentBoard(board2);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeader(out, color);
        out.println();
        if (color.equalsIgnoreCase("BLACK")) {
            String[][] board = drawBoardFromBlack(out);
            drawBoard(out,board,color, Row2, moves);
        } else if(color.equalsIgnoreCase("WHITE")) {
            String[][] board = drawBoardFromWhite(out);
            drawBoard(out,board,color, Row2, moves);
        }
        drawHeader(out, color);
        Row1 = new ArrayList<>();
         Row2 = new ArrayList<>();
         Row3 = new ArrayList<>();
         Row4 = new ArrayList<>();
         Row5 = new ArrayList<>();
        Row6 = new ArrayList<>();
        Row7 = new ArrayList<>();
        Row8 = new ArrayList<>();
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
        String[] row1Array = Row1.toArray(new String[0]);
        String[] row2Array = Row2.toArray(new String[0]);
        String[] row3Array = Row3.toArray(new String[0]);
        String[] row4Array = Row4.toArray(new String[0]);
        String[] row5Array = Row5.toArray(new String[0]);
        String[] row6Array = Row6.toArray(new String[0]);
        String[] row7Array = Row7.toArray(new String[0]);
        String[] row8Array = Row8.toArray(new String[0]);
        String[][] board = {
                row8Array,
                row7Array,
                row6Array,
                row5Array,
                row4Array,
                row3Array,
                row2Array,
                row1Array


        };
        out.print(SET_TEXT_COLOR_BLUE);
        return board;
    }

    static String[][] drawBoardFromWhite(PrintStream out) {
        String[] row1Array = Row1.toArray(new String[0]);
        String[] row2Array = Row2.toArray(new String[0]);
        String[] row3Array = Row3.toArray(new String[0]);
        String[] row4Array = Row4.toArray(new String[0]);
        String[] row5Array = Row5.toArray(new String[0]);
        String[] row6Array = Row6.toArray(new String[0]);
        String[] row7Array = Row7.toArray(new String[0]);
        String[] row8Array = Row8.toArray(new String[0]);
        String[][] board = {
                row1Array,
                row2Array,
                row3Array,
                row4Array,
                row5Array,
                row6Array,
                row7Array,
                row8Array
        };
        return board;
    }

    static void drawBoard(PrintStream out, String[][] board, String color, List<String> row2, Collection<ChessMove> moves) {
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
                if (moves != null){
                    boolean possibleMove = false;
                    for (ChessMove move : moves){
                        ChessPosition startPosition = move.getStartPosition();
                        int row3 = 0;
                        if(color.equalsIgnoreCase("WHITE")) {
                             row3 = 9 - startPosition.getRow() - 1;
                        } else{
                            row3 = startPosition.getRow() -1;
                        }
                        int col3 = startPosition.getColumn()-1;

                        if (row == row3 && col == startPosition.getColumn()-1){
                            out.print(SET_BG_COLOR_MAGENTA);
                            possibleMove = true;
                        }
                        ChessPosition endPosition = move.getEndPosition();
                        int row4 = 0;
                        if(color.equalsIgnoreCase("WHITE")) {
                             row4 = 9 - endPosition.getRow()-1;
                        } else{
                             row4 = endPosition.getRow()-1;
                        }
                        int col4 =  endPosition.getColumn()-1;
                        if (row == row4 && col == col4){
                            out.print(SET_BG_COLOR_MAGENTA);
                            possibleMove = true;
                        }
                    } if(!possibleMove){
                        if (isDarkSquare) {
                            out.print(SET_BG_COLOR_LIGHT_GREY);
                        } else {
                            out.print(SET_BG_COLOR_DARK_GREY);
                        }
                    }
                }else{
                    if (isDarkSquare) {
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    } else {
                        out.print(SET_BG_COLOR_DARK_GREY);
                    }
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
