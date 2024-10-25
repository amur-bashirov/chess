package chess;

import java.util.ArrayList;

public class KnightCalculator implements PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    public KnightCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    void updateMoves(ArrayList<ChessMove> moves, ChessPosition position, ChessGame.TeamColor color) {
        int row = position.getRow();
        int col = position.getColumn();
        if (row > 0 && row < 9 && col > 0 && col < 9) {
            if (this.board.getPiece(position) != null) {
                if (this.board.getPiece(position).getTeamColor() != color) {
                    moves.add(new ChessMove(this.position, new ChessPosition(row, col), null));
                    return;
                } else{
                    return;
                }
            }
            moves.add(new ChessMove(this.position, new ChessPosition(row, col), null));
        }
    }

    @Override
    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessPosition> positions = new ArrayList<>();
        ChessPosition newPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
        positions.add(newPosition);
        newPosition = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
        positions.add(newPosition);
        newPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
        positions.add(newPosition);
        newPosition = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
        positions.add(newPosition);
        newPosition = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
        positions.add(newPosition);
        newPosition = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
        positions.add(newPosition);
        newPosition = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
        positions.add(newPosition);
        newPosition = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
        positions.add(newPosition);
        for (ChessPosition p : positions) {
            updateMoves(moves, p, color);
        }

        return moves;
    }



}
