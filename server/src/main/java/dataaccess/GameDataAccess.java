package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;
import objects.OccupiedException;

import java.util.ArrayList;

public interface GameDataAccess {

    ArrayList<GameData> listGames() throws DataAccessException;
    GameData getGame(String gameName) throws DataAccessException;
    GameData createGame(String gameName) throws dataaccess.DataAccessException;
    GameData getGame2(int gameID) throws dataaccess.DataAccessException;
    void updateGame(String gameColor, int gameID, String username) throws OccupiedException, DataAccessException;
    void deletePlayer(String color, int gameId, String username) throws DataAccessException, OccupiedException;
    void updateGame2(int gameID, ChessGame game) throws DataAccessException;
    void clear() throws dataaccess.DataAccessException;

}
