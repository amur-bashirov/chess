package dataaccess;

import DataObjects.DataAccessException;
import model.GameData;
import DataObjects.OccupiedException;

import java.util.ArrayList;

public interface GameDataAccess {

    ArrayList<GameData> listGames() throws DataAccessException;
    GameData getGame(String gameName) throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame2(int gameID) throws DataAccessException;
    void updateGame(String gameColor, int gameID, String username) throws OccupiedException, DataAccessException;
    void clear() throws DataAccessException;

}
