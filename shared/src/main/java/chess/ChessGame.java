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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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


        for (ChessMove move : possibleMoves) {
            if (move.getPromotionPiece() != null) {
                ChessPiece piece1 = board.getPiece(move.getStartPosition());
                ChessPiece piece2 = null;
                if (board.getPiece(move.getEndPosition()) != null) {
                    piece2 = board.getPiece(move.getEndPosition());
                }
                board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),
                        move.getPromotionPiece()));
                board.addPiece(move.getStartPosition(), null);
                if (!isInCheck(piece.getTeamColor())) {
                    validMoves.add(move); // If not in check, add the move to validMoves
                }
                board.addPiece(move.getStartPosition(), piece1);
                if(piece2 != null) {
                    board.addPiece(move.getEndPosition(), piece2);
                }
            }else {
                ChessPiece piece1 = board.getPiece(move.getStartPosition());
                ChessPiece piece2 = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), piece1);
                board.addPiece(move.getStartPosition(), null);
                if (!isInCheck(piece.getTeamColor())) {
                    validMoves.add(move); // If not in check, add the move to validMoves
                }
                board.addPiece(move.getStartPosition(), piece1);
                board.addPiece(move.getEndPosition(), piece2);

            }
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
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }
        if (validMoves.contains(move) && board.getPiece(move.getStartPosition()).getTeamColor() == teamTurn) {

            if (move.getPromotionPiece() != null) {
                board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),
                        move.getPromotionPiece()));
                board.addPiece(move.getStartPosition(), null);
            }else {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(), null);
            }
            if (teamTurn == TeamColor.BLACK){
                teamTurn = TeamColor.WHITE;
            }else{
                teamTurn = TeamColor.BLACK;
            }

        }
        else{
            throw new InvalidMoveException();
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int i = 1; i <9;i++ ){
            for (int j = 1;j<9;j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                            kingPosition = new ChessPosition(i, j);
                            break;
                        }
                    }
                }
            }
            if (kingPosition != null) {
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
                            if (movesArray[k].getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean cheakValidMoves(TeamColor teamColor) {
        for (int i = 1; i <9;i++ ){
            for (int j = 1;j<9;j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        Collection <ChessMove> validMoves = validMoves(new ChessPosition(i,j));
                        if (validMoves.size() > 0) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        return cheakValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        for (int i = 1; i <9;i++ ){
            for (int j = 1;j<9;j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        Collection <ChessMove> validMoves = validMoves(new ChessPosition(i,j));
                        if (validMoves.size() > 0) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
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
