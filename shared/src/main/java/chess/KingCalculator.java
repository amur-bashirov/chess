package chess;

import java.util.ArrayList;

public class KingCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    public KingCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    void update_moves(ArrayList<ChessMove> moves, ChessPosition position, ChessGame.TeamColor color) {
        int row = position.getRow();
        int col = position.getColumn();
        if (row > 0 && row < 9 && col > 0 && col < 9) {
            if (this.board.getPiece(position) != null) {
                if (this.board.getPiece(position).getTeamColor() != color) {
                    moves.add(new ChessMove(this.myPosition, new ChessPosition(row, col), null));
                    return;
                } else{
                    return;
                }
            }
            moves.add(new ChessMove(this.myPosition, new ChessPosition(row, col), null));
        }
    }


    @Override
    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int start = position.getColumn()-1;
        for (int i = 0; i < 3; i++) {
            update_moves(moves, new ChessPosition(position.getRow()+1, start+i), board.getPiece(position).getTeamColor());
            update_moves(moves, new ChessPosition(position.getRow()-1, start+i), board.getPiece(position).getTeamColor());
        }
        update_moves(moves, new ChessPosition(position.getRow(), position.getColumn()+1), board.getPiece(position).getTeamColor());
        update_moves(moves, new ChessPosition(position.getRow(), position.getColumn()-1), board.getPiece(position).getTeamColor());

        return moves;
    }
}
