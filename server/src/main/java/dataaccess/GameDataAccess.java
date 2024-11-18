package dataaccess;

import model.GameData;
import dataObjects.OccupiedException;

import java.util.ArrayList;

public interface GameDataAccess {

    ArrayList<GameData> listGames() throws DataAccessException;
    GameData getGame(String gameName) throws DataAccessException;
    GameData createGame(String gameName) throws dataaccess.DataAccessException;
    GameData getGame2(int gameID) throws dataaccess.DataAccessException;
    void updateGame(String gameColor, int gameID, String username) throws OccupiedException, DataAccessException;
    void clear() throws dataaccess.DataAccessException;

}
