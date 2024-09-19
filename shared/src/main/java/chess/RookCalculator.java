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
        boolean took_left = false;
        boolean took_right = false;
        boolean took_up = false;
        boolean took_down = false;
        int left = 8- position.getRow() + 1;
        for (int i = 1; i < left; i++) {
            if (!took_up) {
                ChessPosition newPosition = new ChessPosition(position.getRow() +i, position.getColumn());
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_up = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        took_up = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        for (int i = 1; i < position.getRow();i++) {
            if (!took_down) {
                ChessPosition newPosition = new ChessPosition(position.getRow() -i, position.getColumn());
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_down = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        took_down = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        int left2 = 8- position.getColumn();
        for (int i = 1; i < left2+1; i++) {
            if (!took_right){
                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_right = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        took_right = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        for (int i = 1; i < position.getColumn();i++) {
            if (!took_left) {
                ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() -i);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        took_left = true;
                        moves.add(new ChessMove(position, newPosition,null));
                    } else if (board.getPiece(newPosition).getTeamColor() == color) {
                        took_left = true;
                    }
                } else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return moves;
    }
}
