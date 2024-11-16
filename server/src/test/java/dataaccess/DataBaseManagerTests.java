package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import DataObjects.OccupiedException;

import java.util.ArrayList;


public class DataBaseManagerTests {
    private static DatabaseManager db;
    private final UserDataAccess userAccess = new MySqlUserDAO();
    private final AuthDataAccess authAccess = new MySqlAuthDAO();
    private final GameDataAccess gameAccess = new MySqlGameDAO();
    private final ClearAccess clearAccess = new MySqlClear();

    public DataBaseManagerTests() throws dataaccess.DataAccessException {
    }

    @BeforeAll
    public static void createDataBase() throws dataaccess.DataAccessException {
        db = new DatabaseManager();
        db.openConnection();
        db.configureDatabase();
        db.closeConnection(true);
    }

    @BeforeEach
    public void settingUp() throws dataaccess.DataAccessException {
        db.openConnection();
        userAccess.clear();
        authAccess.clear();
        gameAccess.clear();
    }
    @AfterEach
    public void tearDown() throws dataaccess.DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void successCreateUserTest() throws dataaccess.DataAccessException {
        UserData data = new UserData("username", "password", "email");
        userAccess.creatUser(data);
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertEquals(data, data2);
    }

    @Test
    public void failCreateUserTest() throws dataaccess.DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        UserData data2 =new UserData("test", "test", "test");
        Assertions.assertThrows(dataaccess.DataAccessException.class, () -> userAccess.creatUser(data2));
        userAccess.clear();
    }

    @Test
    public void clearTest() throws dataaccess.DataAccessException {
        UserData newData = new UserData("username", "password", "email");
        userAccess.creatUser(newData);
        userAccess.clear();
        UserData data2 = userAccess.getUser(newData.username());
        Assertions.assertNull(data2);
    }

    @Test
    public void failGetUserTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        userAccess.clear();
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertNull(data2);
        userAccess.clear();
    }

    @Test
    public void successGetUserTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertEquals(data, data2);
        userAccess.clear();
    }

    @Test
    public void successGetAuthTest() throws dataaccess.DataAccessException {
        UserData data = new UserData("username", "password", "email");
        userAccess.creatUser(data);
        AuthData auth = authAccess.createAuth(data);
        AuthData auth2 = authAccess.getAuth(auth.authToken());
        Assertions.assertEquals(auth, auth2);
    }

    @Test
    public void failGetAuthTest() throws dataaccess.DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        AuthData auth = authAccess.createAuth(data);
        authAccess.deleteAuth(auth);
        AuthData auth2 = authAccess.getAuth(auth.authToken());
        Assertions.assertNull(auth2);
    }

    @Test
    public void failCreateAuthTest() throws dataaccess.DataAccessException {
        AuthData auth = authAccess.createAuth(null);
        Assertions.assertNull(auth);
        userAccess.clear();
        authAccess.clear();
    }

    @Test
    public void successCreateAuthTest() throws dataaccess.DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        AuthData auth = authAccess.createAuth(data);
        AuthData auth2 = authAccess.getAuth(auth.authToken());
        Assertions.assertEquals(auth, auth2);
        userAccess.clear();
        authAccess.clear();
    }

    @Test
    public void successDeleteAuthTest() throws dataaccess.DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        AuthData auth = authAccess.createAuth(data);
        authAccess.deleteAuth(auth);
        AuthData auth2 = authAccess.getAuth(auth.authToken());
        Assertions.assertNull(auth2);
        userAccess.clear();
        authAccess.clear();
    }



    @Test
    public void successClearTest() throws dataaccess.DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        userAccess.clear();
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertNull(data2);
    }

    @Test
    public void successCreateGameTest() throws dataaccess.DataAccessException {
        String gameName = "test";


        GameData createdGame = gameAccess.createGame(gameName);


        GameData retrievedGame = gameAccess.getGame(gameName);


        Assertions.assertNotNull(retrievedGame,
                "Retrieved game should not be null");
        Assertions.assertTrue(retrievedGame.gameID() > 0,
                "Game ID should be greater than 0");
        Assertions.assertEquals(gameName,
                retrievedGame.gameName(), "Game name should match the one created");


        gameAccess.clear();
    }


    @Test
    public void failCreateGameWithEmptyNameTest() throws DataAccessException {
        String emptyGameName = "";
        GameData result = gameAccess.createGame(emptyGameName);
        Assertions.assertNull(result, "Expected result to be null for empty game name.");
    }

    @Test
    public void successGetGameTest() throws dataaccess.DataAccessException {
        ChessGame game = new ChessGame();
        String gameName = "test";
        GameData createdGame = gameAccess.createGame(gameName);
        GameData data2 = gameAccess.getGame(gameName);
        Assertions.assertEquals(createdGame, data2);
        gameAccess.clear();
    }

    @Test
    public void failGetGameTest() throws dataaccess.DataAccessException {
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.clear();
        GameData data2 = gameAccess.getGame(gameName);
        Assertions.assertNull(data2, "Expected no game to be found after clearing.");
        gameAccess.clear();
    }

    @Test
    public void successGameClearTest() throws dataaccess.DataAccessException {
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.clear();
        GameData data2 = gameAccess.getGame(gameName);
        Assertions.assertNull(data2, "Expected no game to be found after clearing.");
    }

    @Test
    public void successUpdateGameTest() throws dataaccess.DataAccessException, OccupiedException {
        ChessGame game = new ChessGame();
        String gameName = "test";
        String userName = "user";
        GameData createdGame = gameAccess.createGame(gameName);

        gameAccess.updateGame("WHITE", createdGame.gameID(), "user"); // Use the dynamically retrieved game ID
        GameData updatedGame = gameAccess.getGame(gameName);

        Assertions.assertEquals(updatedGame.whiteUsername(),userName);
        Assertions.assertNull(updatedGame.blackUsername());
        Assertions.assertEquals(createdGame.gameName(), updatedGame.gameName());
        Assertions.assertEquals(createdGame.game(), updatedGame.game());

        gameAccess.clear();
    }

    @Test
    public void failUpdateGameTest() throws dataaccess.DataAccessException, OccupiedException {
        String gameName = "test";
        GameData createdGame = gameAccess.createGame(gameName); // Create the game
        gameAccess.updateGame("WHITE", createdGame.gameID(), "test1"); // Update the game

        Assertions.assertThrows(OccupiedException.class, () -> {
            gameAccess.updateGame("WHITE", createdGame.gameID(), "test2"); // Attempt to update again
        });

        gameAccess.clear();
    }


    @Test
    public void succeessListGamesTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        String gameName = "test";
        gameAccess.createGame(gameName);
        ArrayList<GameData> gameList = gameAccess.listGames();
        for (GameData game1 : gameList){
            Assertions.assertEquals(gameName, game1.gameName());


        }

        gameAccess.clear();
    }

    @Test
    public void failListGamesTest() throws dataaccess.DataAccessException {
        ChessGame game = new ChessGame();
        GameData data = new GameData(1,
                "test",null,"test",game);
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.clear();
        ArrayList<GameData> gameList = gameAccess.listGames();
        Assertions.assertFalse(gameList.contains(data));
        gameAccess.clear();
    }

    @Test
    public void successGetGame2Test() throws dataaccess.DataAccessException {
        ChessGame game = new ChessGame();
        String gameName = "test";
        GameData createdGameData = gameAccess.createGame(gameName);
        GameData fetchedGameData = gameAccess.getGame2(createdGameData.gameID());
        Assertions.assertEquals(createdGameData, fetchedGameData);
        gameAccess.clear();
    }


    @Test
    public void failGetGame2Test() throws dataaccess.DataAccessException {
        ChessGame game = new ChessGame();
        GameData data = new GameData(1, "test",null,"test",game);
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.clear();
        GameData data2 = gameAccess.getGame2(1);
        Assertions.assertNull(data2);
        gameAccess.clear();
    }

    @Test
    public void successMySqlClearTest() throws DataAccessException {
        String gameName = "test";
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        gameAccess.createGame(gameName);
        clearAccess.clear();
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertNull(data2);
        Assertions.assertNull(gameAccess.getGame(gameName));
    }

}
