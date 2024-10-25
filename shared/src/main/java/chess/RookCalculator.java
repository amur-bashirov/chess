package chess;

import java.util.ArrayList;

public class RookCalculator implements PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    public RookCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    @Override
    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        boolean tookLeft = false;
        boolean tookRight = false;
        boolean tookUp = false;
        boolean tookDown = false;
        int left = 8- position.getRow() + 1;
        for (int i = 1; i < left; i++) {
            if (!tookUp) {
                ChessPosition newPosition = new ChessPosition(position.getRow() +i, position.getColumn());
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookUp = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        tookUp = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        for (int i = 1; i < position.getRow();i++) {
            if (!tookDown) {
                ChessPosition newPosition = new ChessPosition(position.getRow() -i, position.getColumn());
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookDown = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        tookDown = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        int left2 = 8- position.getColumn();
        for (int i = 1; i < left2+1; i++) {
            if (!tookRight){
                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookRight = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        tookRight = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        for (int i = 1; i < position.getColumn();i++) {
            if (!tookLeft) {
                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() -i);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        tookLeft = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        tookLeft = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return moves;
    }
}
