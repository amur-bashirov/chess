package chess;

import java.util.ArrayList;

public class QueenCalculator implements PieceMovesCalculator {


    private final ChessBoard board;
    private final ChessPosition position;
    public QueenCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    @Override
    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator calculator;
        calculator = new BishopCalculator(board, position);
        ArrayList<ChessMove> moves = calculator.calculateMoves(board, position);
        calculator = new RookCalculator(board, position);
        moves.addAll(calculator.calculateMoves(board, position));
        return moves;
    }
//        ArrayList<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
//        boolean tookLeftUp = false;
//        boolean tookRightUp = false;
//        int left = 8- position.getRow() + 1;
//        for (int i = 1; i < left; i++) {
//            int newRow = position.getRow() + i;
//            int addedCol = position.getColumn() + i;
//            int removedCol = position.getColumn() - i;
//            if ( addedCol < 9 && !tookRightUp){
//                ChessPosition newPosition = new ChessPosition(newRow , addedCol );
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookRightUp = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    }
//                    else if(board.getPiece(newPosition).getTeamColor() == color){
//                        tookRightUp = true;
//                    }
//                } else {
//                    moves.add(new ChessMove(position, newPosition,null));
//                }
//
//            }
//            if (removedCol > 0 && !tookLeftUp){
//                ChessPosition newPosition = new ChessPosition(newRow , removedCol );
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookLeftUp = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    }
//                    else if(board.getPiece(newPosition).getTeamColor() == color){
//                        tookLeftUp = true;}
//                } else {
//                    moves.add(new ChessMove(position, newPosition,null));
//                }
//
//            }
//        }
//        boolean tookLeftDown = false;
//        boolean tookRightDown = false;
//        for (int i = 1; i < position.getRow();i++) {
//            int newRow = position.getRow() - i;
//            int addedCol = position.getColumn() + i;
//            int removedCol = position.getColumn() - i;
//            if ( addedCol < 9 && !tookRightDown){
//                ChessPosition newPosition = new ChessPosition(newRow , addedCol );
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookRightDown = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    }
//                    else if(board.getPiece(newPosition).getTeamColor() == color){
//                        tookRightDown = true;
//                    }
//                } else {
//                    moves.add(new ChessMove(position, newPosition, null));
//                }
//
//            }
//            if (removedCol > 0 && !tookLeftDown){
//                ChessPosition newPosition = new ChessPosition(newRow , removedCol );
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookLeftDown = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    }
//                    else if(board.getPiece(newPosition).getTeamColor() == color){
//                        tookLeftDown = true;
//                    }
//                } else{
//                    moves.add(new ChessMove(position, newPosition,null));
//                }
//
//            }
//        }
//        //now the rook moves
//        boolean tookLeft = false;
//        boolean tookRight = false;
//        boolean tookUp = false;
//        boolean tookDown = false;
//        for (int i = 1; i < left; i++) {
//            if (!tookUp) {
//                ChessPosition newPosition = new ChessPosition(position.getRow() +i, position.getColumn());
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookUp = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
//                        tookUp = true;
//                    }
//                } else {
//                    moves.add(new ChessMove(position, newPosition, null));
//                }
//            }
//        }
//        for (int i = 1; i < position.getRow();i++) {
//            if (!tookDown) {
//                ChessPosition newPosition = new ChessPosition(position.getRow() -i, position.getColumn());
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookDown = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
//                        tookDown = true;
//                    }
//                } else {
//                    moves.add(new ChessMove(position, newPosition, null));
//                }
//            }
//        }
//        int left2 = 8- position.getColumn();
//        for (int i = 1; i < left2+1; i++) {
//            if (!tookRight){
//                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookRight = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
//                        tookRight = true;
//                    }
//                } else {
//                    moves.add(new ChessMove(position, newPosition, null));
//                }
//            }
//        }
//        for (int i = 1; i < position.getColumn();i++) {
//            if (!tookLeft) {
//                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() -i);
//                if (board.getPiece(newPosition) != null) {
//                    if (board.getPiece(newPosition).getTeamColor() != color) {
//                        tookLeft = true;
//                        moves.add(new ChessMove(position, newPosition,null));
//                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
//                        tookLeft = true;
//                    }
//                } else {
//                    moves.add(new ChessMove(position, newPosition, null));
//                }
//            }
//        }
//        return moves;
//    }
}
