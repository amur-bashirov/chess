package chess;
import chess.ChessPiece;

import java.util.ArrayList;

public interface PieceMovesCalculator {







    public  ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition position);



}
