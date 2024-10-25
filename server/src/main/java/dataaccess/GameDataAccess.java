package dataaccess;

import model.GameData;
import service.OccupiedException;

import java.util.ArrayList;

public interface GameDataAccess {

    ArrayList<GameData> listGames();
    GameData getGame(String gameName);
    GameData createGame(String gameName);
    GameData getGame2(int gameID);
    void updateGame(String gameColor, int gameID, String username) throws OccupiedException;
    void clear();

}
