package dataaccess;

import model.GameData;
import java.util.ArrayList;

public interface GameDataAccess {

    ArrayList<GameData> listGames();
    GameData getGame(String gameName);
    void createGame(String gameName);
    GameData getGame2(int gameID);
    void updateGame(String gameColor, int gameID, String username);
    void clear();

}
