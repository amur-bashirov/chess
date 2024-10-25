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
}
