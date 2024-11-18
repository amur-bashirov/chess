package objects;

import model.GameData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList <GameData> games) {
    public int size() {
        int size = games.size();
        return size;
    }
}
