package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public String toString() {
        return "ChessPiece{" +
                "teamColor=" + teamColor +
                ", type=" + type +
                '}';
    }

    private final ChessGame.TeamColor teamColor;
    private final ChessPiece.PieceType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return type == that.type && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceType type = board.getPiece(myPosition).getPieceType();



        PieceMovesCalculator calculator;

        switch (type) {
            case BISHOP:
                calculator = new BishopCalculator(board, myPosition);
                break;
            case ROOK:
                calculator = new RookCalculator(board, myPosition);
                break;
            case QUEEN:
                calculator = new QueenCalculator(board, myPosition);
                break;
            case KING:
                calculator = new KingCalculator(board, myPosition);
                break;
            case KNIGHT:
                calculator = new KnightCalculator(board, myPosition);
                break;
            case PAWN:
                calculator = new PawnCalculator(board, myPosition);
                break;
            default:
                throw new IllegalArgumentException("Unsupported piece type: " + type);
        }

        return calculator.calculateMoves(board, myPosition);

    }


}
