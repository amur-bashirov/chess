package chess;

import java.util.ArrayList;

public class BishopCalculator implements PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    public BishopCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    @Override
    public String toString() {
        return "BishopCalculator{" +
                "board=" + board +
                ", position=" + position +
                '}';
    }

    @Override
    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        boolean tookLeftUp = false;
        boolean tookRightUp = false;
        int left = 8- position.getRow() + 1;
        for (int i = 1; i < left; i++) {
            int newRow = position.getRow() + i;
            int addedCol = position.getColumn() + i;
            int removedCol = position.getColumn() - i;
            if ( addedCol < 9 && !tookRightUp){
                ChessPosition newPosition = new ChessPosition(newRow , addedCol );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookRightUp = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        tookRightUp = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition,null));
                }

            }
            if (removedCol > 0 && !tookLeftUp){
                ChessPosition newPosition = new ChessPosition(newRow , removedCol );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookLeftUp = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        tookLeftUp = true;}
                } else {
                    moves.add(new ChessMove(position, newPosition,null));
                }

            }
        }
        boolean tookLeftDown = false;
        boolean tookRightDown = false;
        for (int i = 1; i < position.getRow();i++) {
            int newRow = position.getRow() - i;
            int addedCol = position.getColumn() + i;
            int removedCol = position.getColumn() - i;
            if ( addedCol < 9 && !tookRightDown){
                ChessPosition newPosition = new ChessPosition(newRow , addedCol );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookRightDown = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        tookRightDown = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }

            }
            if (removedCol > 0 && !tookLeftDown){
                ChessPosition newPosition = new ChessPosition(newRow , removedCol );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookLeftDown = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        tookLeftDown = true;
                    }
                } else{
                    moves.add(new ChessMove(position, newPosition,null));
                }

            }
        }


        return moves;
    }
}
