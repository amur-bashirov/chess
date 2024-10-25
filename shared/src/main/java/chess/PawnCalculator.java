package chess;

import java.util.ArrayList;

public class PawnCalculator implements PieceMovesCalculator {

    private final ChessBoard board;


    @Override
    public String toString() {
        return "PawnCalculator{" +
                "board=" + board +
                ", position=" + position +
                '}';
    }

    private final ChessPosition position;
    public PawnCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }
    @Override
    public ArrayList<ChessMove> calculateMoves(ChessBoard board,
                                               ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();
        if (color == ChessGame.TeamColor.WHITE) {

            if (row < 7 && col != 8) {
                ChessPosition newposition = new ChessPosition(row + 1, col + 1);
                if (board.getPiece(newposition) != null) {
                    if (board.getPiece(newposition).getTeamColor() != color) {
                        moves.add(new ChessMove(position, newposition, null));
                    }
                }
            }


            if (row < 7 && col != 1 ) {
                ChessPosition newposition = new ChessPosition(row + 1, col - 1);
                if (board.getPiece(newposition) != null) {
                    if (board.getPiece(newposition).getTeamColor() != color) {
                        moves.add(new ChessMove(position, newposition, null));
                    }
                }
            }

            if (row == 7){
                ChessPosition newposition = new ChessPosition(row + 1, col);
                if (board.getPiece(newposition) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col), ChessPiece.PieceType.KNIGHT));
                }
                ChessPosition newposition2 = new ChessPosition(row + 1, col +1);
                if (board.getPiece(newposition2) != null) {
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col+1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col+1), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col+1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col+1), ChessPiece.PieceType.KNIGHT));
                }
                ChessPosition newposition3 = new ChessPosition(row + 1, col -1);
                if (board.getPiece(newposition3) != null) {
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col-1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col-1), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col-1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col-1), ChessPiece.PieceType.KNIGHT));
                }
            }

            ChessPosition newPosition = new ChessPosition(row+1, col);
            if (board.getPiece(newPosition) == null && row != 7) {
                moves.add(new ChessMove(position, newPosition,null));
                ChessPosition newPosition2 = new ChessPosition(row+2, col);
                if (row == 2 && board.getPiece(newPosition2) == null) {
                    moves.add(new ChessMove(position, newPosition2,null));
                }
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {


            if (row > 2 && col != 8) {
                ChessPosition newposition = new ChessPosition(row - 1, col + 1);
                if (board.getPiece(newposition) != null) {
                    if (board.getPiece(newposition).getTeamColor() != color) {
                        moves.add(new ChessMove(position, newposition, null));
                    }
                }
            }


            if (row > 2 && col != 1) {
                ChessPosition newposition = new ChessPosition(row - 1, col - 1);
                if (board.getPiece(newposition) != null) {
                    if (board.getPiece(newposition).getTeamColor() != color) {
                        moves.add(new ChessMove(position, newposition, null));
                    }
                }
            }

            if (row == 2){
                ChessPosition newposition = new ChessPosition(row - 1, col);
                if (board.getPiece(newposition) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col), ChessPiece.PieceType.KNIGHT));
                }
                ChessPosition newposition2 = new ChessPosition(row - 1, col +1);
                if (board.getPiece(newposition2) != null) {
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col+1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col+1), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col+1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col+1), ChessPiece.PieceType.KNIGHT));
                }
                ChessPosition newposition3 = new ChessPosition(row - 1, col -1);
                if (board.getPiece(newposition3) != null) {
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col-1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col-1), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col-1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(row-1, col-1), ChessPiece.PieceType.KNIGHT));
                }
            }


            ChessPosition newPosition = new ChessPosition(row-1, col);
            if (board.getPiece(newPosition) == null && row != 2) {
                moves.add(new ChessMove(position, newPosition,null));
                ChessPosition newPosition2 = new ChessPosition(row-2, col);
                if (row == 7 && board.getPiece(newPosition2) == null) {
                    moves.add(new ChessMove(position, newPosition2,null));
                }
            }
        }



        return moves;
    }
}
