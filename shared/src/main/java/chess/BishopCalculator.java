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
        boolean took_left_up = false;
        boolean took_right_up = false;
        int left = 8- position.getRow() + 1;
        for (int i = 1; i < left; i++) {
            int newRow = position.getRow() + i;
            int added_Col = position.getColumn() + i;
            int removed_Col = position.getColumn() - i;
            if ( added_Col < 9 && !took_right_up){
                ChessPosition newPosition = new ChessPosition(newRow , added_Col );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_right_up = true;
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        continue;
                    }
                }
                moves.add(new ChessMove(position, newPosition,null));

            }
            if (removed_Col > 0 && !took_left_up){
                ChessPosition newPosition = new ChessPosition(newRow , removed_Col );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_left_up = true;
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        continue;
                    }
                }
                moves.add(new ChessMove(position, newPosition,null));

            }
        }
        boolean took_left_down = false;
        boolean took_right_down = false;
        for (int i = 1; i < position.getRow();i++) {
            int newRow = position.getRow() - i;
            int added_Col = position.getColumn() + i;
            int removed_Col = position.getColumn() - i;
            if ( added_Col < 9 && !took_right_down){
                ChessPosition newPosition = new ChessPosition(newRow , added_Col );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_right_down = true;
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        continue;
                    }
                }
                moves.add(new ChessMove(position, newPosition,null));

            }
            if (removed_Col > 0 && !took_left_down){
                ChessPosition newPosition = new ChessPosition(newRow , removed_Col );
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_left_down = true;
                    }
                    else if(board.getPiece(newPosition).getTeamColor() == color){
                        continue;
                    }
                }
                moves.add(new ChessMove(position, newPosition,null));

            }
        }

        if (moves.size() == 0) {
            return null;
        }
        return moves;
    }
}
