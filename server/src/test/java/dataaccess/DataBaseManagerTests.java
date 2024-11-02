package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.OccupiedException;

import java.util.ArrayList;


public class DataBaseManagerTests {
    private static DatabaseManager db;
    private final UserDataAccess userAccess = new MySqlUserDAO();
    private final AuthDataAccess authAccess = new MySqlAuthDAO();
    private final GameDataAccess gameAccess = new MySqlGameDAO();
    private final ClearAccess clearAccess = new MySqlClear();

    public DataBaseManagerTests() throws DataAccessException {
    }

    @BeforeAll
    public static void createDataBase() throws DataAccessException {
        db = new DatabaseManager();
        db.openConnection();
        db.configureDatabase();
        db.closeConnection(true);
    }

    @BeforeEach
    public void settingUp() throws DataAccessException {
        db.openConnection();
        userAccess.clear();
        authAccess.clear();
        gameAccess.clear();
    }
    @AfterEach
    public void tearDown() throws DataAccessException{
        db.closeConnection(false);
    }

    @Test
    public void successCreateUserTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertEquals(data, data2);
        userAccess.clear();
    }

    @Test
    public void failCreateUserTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        UserData data2 =new UserData("test", "test", "test");
        Assertions.assertThrows(DataAccessException.class, () -> userAccess.creatUser(data2));
        userAccess.clear();
    }

    @Test
    public void clearTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        userAccess.clear();
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertNull(data2);
        userAccess.clear();
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
    public void successGetAuthTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        AuthData auth = authAccess.createAuth(data);
        AuthData auth2 = authAccess.getAuth(auth.authToken());
        Assertions.assertEquals(auth, auth2);
        userAccess.clear();
        authAccess.clear();
    }

    @Test
    public void failGetAuthTest() throws DataAccessException {
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
    public void failCreateAuthTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        AuthData auth = authAccess.createAuth(data);
        Assertions.assertThrows(DataAccessException.class, () -> authAccess.createAuth(data));
        userAccess.clear();
        authAccess.clear();
    }

    @Test
    public void successCreateAuthTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        AuthData auth = authAccess.createAuth(data);
        AuthData auth2 = authAccess.getAuth(auth.authToken());
        Assertions.assertEquals(auth, auth2);
        userAccess.clear();
        authAccess.clear();
    }

    @Test
    public void successDeleteAuthTest() throws DataAccessException {
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
    public void successClearTest() throws DataAccessException {
        UserData data = new UserData("test", "test", "test");
        userAccess.creatUser(data);
        userAccess.clear();
        UserData data2 = userAccess.getUser(data.username());
        Assertions.assertNull(data2);
    }

    @Test
    public void successCreateGameTest() throws DataAccessException {
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
    public void successGetGameTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData data = new GameData(1,null,null,"test",game);
        String gameName = "test";
        gameAccess.createGame(gameName);
        GameData data2 = gameAccess.getGame(gameName);
        Assertions.assertEquals(data, data2);
        gameAccess.clear();
    }

    @Test
    public void failGetGameTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData data = new GameData(1,null,null,"test",game);
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.clear();
        GameData data2 = gameAccess.getGame(gameName);
        Assertions.assertNull(data2);
        gameAccess.clear();
    }

    @Test
    public void successGameClearTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData data = new GameData(1,null,null,"test",game);
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.clear();
        GameData data2 = gameAccess.getGame(gameName);
        Assertions.assertNull(data2);
    }

    @Test
    public void successUpdateGameTest() throws DataAccessException, OccupiedException {
        ChessGame game = new ChessGame();
        GameData data = new GameData(1,"test",null,"test",game);
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.updateGame("WHITE", 1, "test");
        GameData data2 = gameAccess.getGame(gameName);
        Assertions.assertEquals(data, data2);
        gameAccess.clear();
    }

    @Test
    public void failUpdateGameTest() throws DataAccessException, OccupiedException {
        ChessGame game = new ChessGame();
        GameData data = new GameData(1,"test",null,"test",game);
        String gameName = "test";
        gameAccess.createGame(gameName);
        gameAccess.updateGame("WHITE", 1, "test1");
        Assertions.assertThrows(OccupiedException.class, () -> gameAccess.updateGame("WHITE", 1, "test2"));
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
    public void failListGamesTest() throws DataAccessException {
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
    public void successGetGame2Test() throws DataAccessException {
        ChessGame game = new ChessGame();
        String gameName = "test";
        GameData createdGameData = gameAccess.createGame(gameName);
        GameData fetchedGameData = gameAccess.getGame2(createdGameData.gameID());
        Assertions.assertEquals(createdGameData, fetchedGameData);
        gameAccess.clear();
    }


    @Test
    public void failGetGame2Test() throws DataAccessException {
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
    public void SuccessMySqlClearTest() throws DataAccessException {
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
