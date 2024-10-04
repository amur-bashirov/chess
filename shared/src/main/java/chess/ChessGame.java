package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor teamTurn;

    public ChessGame() {
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }


    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>(); // Initialize an ArrayList to hold valid moves

        // Check each possible move
        for (ChessMove move : possibleMoves) {
            ChessPiece piece1 = board.getPiece(move.getStartPosition());
            ChessPiece piece2 = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece1);
            board.addPiece(move.getStartPosition(), null);


            // Check if the king is in check after the move
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move); // If not in check, add the move to validMoves
            }

            board.addPiece(move.getStartPosition(), piece1);
            board.addPiece(move.getEndPosition(), piece2);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPiece piece2 = board.getPiece(move.getEndPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
        if (isInCheck(piece.getTeamColor())) {
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), piece2);
            throw new InvalidMoveException("King is in check");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king_position = null;
        for (int i = 1; i <9;i++ ){
            for (int j = 1;j<9;j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                        king_position = new ChessPosition(i, j);
                        break;
                    }
                }
            }
            if (king_position != null) {
                break;
            }
        }
        for (int i = 1; i <9;i++ ){
            for (int j = 1;j<9;j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        Collection<ChessMove> moves = piece.pieceMoves(board, new ChessPosition(i, j));
                        ChessMove[] movesArray = moves.toArray(new ChessMove[0]);
                        for (int k = 0; k < movesArray.length; k++) {
                            if (movesArray[k].getEndPosition().equals(king_position)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition king_position = null;
        ChessMove[] KingMoves = null;
        for (int i = 1; i <9;i++ ){
            for (int j = 1;j<9;j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                        king_position = new ChessPosition(i, j);
                        Collection<ChessMove> king_moves = piece.pieceMoves(board, new ChessPosition(i, j));
                        KingMoves = king_moves.toArray(new ChessMove[0]);
                        break;
                    }
                }
            }
            if (king_position != null) {
                break;
            }
        }
        int n = KingMoves.length;
        for (int p = 0;p < KingMoves.length;p++){
            ChessPiece important_piece = board.getPiece(KingMoves[p].getEndPosition());
            board.addPiece(KingMoves[p].getEndPosition(), board.getPiece(king_position));
            board.addPiece(king_position, null);
            boolean isInCheck = false;

            for (int i = 1; i <9;i++ ){
                for (int j = 1;j<9;j++){
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    if (piece != null) {
                        if (piece.getTeamColor() != teamColor) {
                            Collection<ChessMove> piece_moves = piece.pieceMoves(board, new ChessPosition(i, j));
                            ChessMove[] movesArray = piece_moves.toArray(new ChessMove[0]);
                            for (int k = 0; k < movesArray.length; k++) {
                                if (movesArray[k].getEndPosition().equals(KingMoves[p].getEndPosition())) {
                                    isInCheck = true;
                                }
                            }
                        }
                    }
                }
            }
            board.addPiece(king_position, board.getPiece(KingMoves[p].getEndPosition()));
            board.addPiece(KingMoves[p].getEndPosition(), important_piece);
            if (!isInCheck){
                return false;
            }

        }
        ChessMove [] teamMoves = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> piece_moves = piece.pieceMoves(board, new ChessPosition(i, j));
                        teamMoves = piece_moves.toArray(new ChessMove[0]);
                        break;
                    }
                }
            }
            if (teamMoves != null) {
                break;
            }
        }

        for (int i = 0; i < teamMoves.length; i++) {
            ChessPiece important_piece = board.getPiece(teamMoves[i].getEndPosition());
            board.addPiece(teamMoves[i].getEndPosition(), board.getPiece(teamMoves[i].getStartPosition()));
            board.addPiece(teamMoves[i].getStartPosition(), null);
            boolean isInCheck = false;
            for (int j = 1; j < 9; j++) {
                for (int k = 1; k < 9; k++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(j, k));
                    if (piece != null) {
                        if (piece.getTeamColor() != teamColor) {
                            Collection<ChessMove> piece_moves = piece.pieceMoves(board, new ChessPosition(j, k));
                            ChessMove[] movesArray = piece_moves.toArray(new ChessMove[0]);
                            for (int l = 0; l < movesArray.length; l++) {
                                if (movesArray[l].getEndPosition().equals(king_position)) {
                                    isInCheck = true;
                                }
                            }
                        }
                    }
                }
            }
            board.addPiece(teamMoves[i].getStartPosition(), board.getPiece(teamMoves[i].getEndPosition()));
            board.addPiece(teamMoves[i].getEndPosition(), important_piece);
            if (!isInCheck) {
                return false;
            }
        }







        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
