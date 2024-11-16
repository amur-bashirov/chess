package dataaccess;

import DataObjects.DataAccessException;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import DataObjects.OccupiedException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySqlGameDAO implements GameDataAccess {


    public MySqlGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList();
        try(var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT gameId, " +
                    "whiteUsername, blackUsername, gameName, gameJson FROM game")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        GameData newGame = convertGame(rs);
                        gameList.add(newGame);
                    }
                    return gameList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData convertGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameId");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String gameJson = rs.getString("gameJson");

        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, whiteUsername, blackUsername, gameName," +
                    " gameJson FROM game WHERE gameName=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameName);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        GameData game = convertGame(rs);
                        return game;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        String jsonString = new Gson().toJson(game);
        var statement = "INSERT INTO game (gameName, gameJson) VALUES (?, ?)";
        if (gameName == null || gameName == ""){
            return null;
        }
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {



            ps.setString(1, gameName);
            ps.setString(2, jsonString);


            ps.executeUpdate();


            try (var generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int gameID = generatedKeys.getInt(1);
                    return new GameData(gameID, null, null, gameName, game);
                } else {
                    throw new DataAccessException("Failed to retrieve generated gameID.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database error while creating game: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame2(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, whiteUsername, blackUsername," +
                    " gameName, gameJson FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        GameData newGame = convertGame(rs);
                        return newGame;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void updateGame(String gameColor, int gameID,
                           String username) throws OccupiedException, DataAccessException {

        GameData data = getGame2(gameID);
        if (gameColor.equalsIgnoreCase( "WHITE") && data.whiteUsername() == null) {
            String statement = "UPDATE game SET whiteUsername = ? WHERE gameId = ?";

            executeUpdate(statement, username, gameID);
        } else if (gameColor.equalsIgnoreCase("BLACK") && data.blackUsername() == null) {
            String statement = "UPDATE game SET blackUsername = ? WHERE gameId = ?";
            executeUpdate(statement, username, gameID);
        } else {
            throw new OccupiedException("Game is already full");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }
}
