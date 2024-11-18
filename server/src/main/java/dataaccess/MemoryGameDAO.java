package dataaccess;

import java.util.ArrayList;

import chess.ChessGame;
import model.GameData;
import objects.OccupiedException;

import java.util.Objects;


public class MemoryGameDAO implements GameDataAccess{

    private ArrayList<GameData> gameList = new ArrayList();
    private int gameID = 1;


    @Override
    public String toString() {
        return "MemoryGameDAO{" +
                "gameList=" + gameList +
                '}';
    }

    public ArrayList<GameData> listGames(){
        for (GameData data : gameList){
            System.out.println(data.toString());
        }
        return gameList;
    }

    @Override
    public GameData getGame(String gameName) {
        for (GameData data: gameList){
            System.out.println(data.toString());
            if (data.gameName().equals(gameName)){
                return data;
            }
        }
        return null;
    }

    @Override
    public GameData createGame(String gameName) {
//        UUID uuid = UUID.randomUUID();
//        int GameID = (int) (uuid.getLeastSignificantBits() & 0xFFFFFFFFL);
        if (gameName == null || gameName == ""){
            return null;
        }
        ChessGame game = new ChessGame();
        GameData data = new GameData(gameID++,null,null,gameName,game);
        System.out.println("New game created:" + data.toString());
        gameList.add(data);
        System.out.println(gameList.toString());
        return data;
    }

    @Override
    public GameData getGame2(int gameID) {
        for (GameData data: gameList){
            System.out.println(data.toString());
            if (data.gameID() == gameID){
                return data;
            }
        }
        return null;
    }

    @Override
    public void updateGame(String gameColor, int gameID, String username) throws OccupiedException {
        for (GameData data: gameList){
            System.out.println(data.toString());
            if (data.gameID() == gameID){
                if (Objects.equals(gameColor, "WHITE") && data.whiteUsername() == null){
                    GameData newData = new GameData(gameID,username,data.blackUsername(),data.gameName(), data.game());
                    int index = gameList.indexOf(data);
                    gameList.set(index, newData);
                    System.out.println("Updated list: " + gameList);
                }
                else if (Objects.equals(gameColor, "BLACK") && data.blackUsername() == null){
                    GameData newData = new GameData(gameID,data.whiteUsername(),username,data.gameName(), data.game());
                    int index = gameList.indexOf(data);
                    gameList.set(index, newData);
                    System.out.println("Updated list: " + gameList);
                }
                else{
                    throw new OccupiedException("already taken");
                }
            }
        }
    }

    @Override
    public void clear() {
        gameList.clear();
    }
}
