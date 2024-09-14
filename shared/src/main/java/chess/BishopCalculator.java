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
        int left = 7- position.getRow() - 1;
        for (int i = 1; i < left; i++) {
            int newRow = position.getRow() + i;
            int added_Col = position.getColumn() + i;
            int removed_Col = position.getColumn() - i;
            if ( added_Col < 7){
                ChessPosition newPosition = new ChessPosition(newRow, added_Col);
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(position, newPosition,null));
                }
            }
            if (removed_Col > 0){
                ChessPosition newPosition = new ChessPosition(newRow, added_Col);
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(position, newPosition,null));
                }
            }
        }
        for (int i = 1; i < position.getRow()+1;i++) {
            int newRow = position.getRow() - i;
            int added_Col = position.getColumn() + i;
            int removed_Col = position.getColumn() - i;
            if ( added_Col < 7){
                ChessPosition newPosition = new ChessPosition(newRow, added_Col);
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(position, newPosition,null));
                }
            }
            if (removed_Col > 0){
                ChessPosition newPosition = new ChessPosition(newRow, added_Col);
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(position, newPosition,null));
                }
            }
        }

        if (moves.size() == 0) {
            return null;
        }
        return moves;
    }
}
