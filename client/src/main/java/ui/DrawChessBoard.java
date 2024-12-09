package ui;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private final String color;

    private static  List<String> row1 = new ArrayList<>();
    private static  List<String> row2 = new ArrayList<>();
    private static List<String> row3 = new ArrayList<>();
    private static List<String> row4 = new ArrayList<>();
    private static List<String> row5 = new ArrayList<>();
    private static List<String> row6 = new ArrayList<>();
    private static List<String> row7 = new ArrayList<>();
    private static List<String> row8 = new ArrayList<>();

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
                                break;}
                            case PAWN: {
                                piece2 = WHITE_PAWN;
                                break;}
                            case KING: {
                                piece2 = WHITE_KING;
                                break;}
                            case QUEEN: {
                                piece2 = WHITE_QUEEN;
                                break;}
                            case ROOK: {
                                piece2 = WHITE_ROOK;
                                break;}
                            case KNIGHT: {
                                piece2 = WHITE_KNIGHT;
                                break;}
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
                            row1.add(piece2);
                            break;}
                        case 1:{
                            row2.add(piece2);
                            break;
                        }
                        case 2:{
                            row3.add(piece2);
                            break;
                        }case 3:{
                            row4.add(piece2);
                            break;
                        }case 4:{
                            row5.add(piece2);
                            break;
                        }
                        case 5:{
                            row6.add(piece2);
                            break;
                        }case 6:{
                            row7.add(piece2);
                            break;
                        } case 7:{
                            row8.add(piece2);
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
            drawBoard(out,board,color, row2, moves);
        } else if(color.equalsIgnoreCase("WHITE")) {
            String[][] board = drawBoardFromWhite(out);
            drawBoard(out,board,color, row2, moves);
        }
        drawHeader(out, color);
        row1 = new ArrayList<>();
         row2 = new ArrayList<>();
         row3 = new ArrayList<>();
         row4 = new ArrayList<>();
         row5 = new ArrayList<>();
        row6 = new ArrayList<>();
        row7 = new ArrayList<>();
        row8 = new ArrayList<>();
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
        Collections.reverse(row1);
        String[] row1Array = row1.toArray(new String[0]);
        Collections.reverse(row2);
        String[] row2Array = row2.toArray(new String[0]);
        Collections.reverse(row3);
        String[] row3Array = row3.toArray(new String[0]);
        Collections.reverse(row4);
        String[] row4Array = row4.toArray(new String[0]);
        Collections.reverse(row5);
        String[] row5Array = row5.toArray(new String[0]);
        Collections.reverse(row6);
        String[] row6Array = row6.toArray(new String[0]);
        Collections.reverse(row7);
        String[] row7Array = row7.toArray(new String[0]);
        Collections.reverse(row8);
        String[] row8Array = row8.toArray(new String[0]);
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
        out.print(SET_TEXT_COLOR_BLUE);
        return board;
    }

    static String[][] drawBoardFromWhite(PrintStream out) {
        String[] row1Array = row1.toArray(new String[0]);
        String[] row2Array = row2.toArray(new String[0]);
        String[] row3Array = row3.toArray(new String[0]);
        String[] row4Array = row4.toArray(new String[0]);
        String[] row5Array = row5.toArray(new String[0]);
        String[] row6Array = row6.toArray(new String[0]);
        String[] row7Array = row7.toArray(new String[0]);
        String[] row8Array = row8.toArray(new String[0]);
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
                        int row3;
                        int col3 = 0;
                        if(color.equalsIgnoreCase("WHITE")) {
                            row3 = 9 - startPosition.getRow()-1;
                            col3 = startPosition.getColumn()-1;
                        } else{
                            col3 = 9-startPosition.getColumn()-1;
                            row3 = startPosition.getRow()-1;
                        }
                        if (row == row3 && col == col3){
                            out.print(SET_BG_COLOR_MAGENTA);
                            possibleMove = true;
                        }
                        ChessPosition endPosition = move.getEndPosition();
                        int row4 = 0;
                        int col4 = 0;
                        if(color.equalsIgnoreCase("WHITE")) {
                             row4 = 9 - endPosition.getRow()-1;
                            col4 =  endPosition.getColumn()-1;
                        } else{
                             row4 = endPosition.getRow()-1;
                             col4 = 9 - endPosition.getColumn()-1;
                        }
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
