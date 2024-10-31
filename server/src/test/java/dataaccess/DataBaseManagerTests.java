package dataaccess;
import model.UserData;
import org.junit.jupiter.api.*;


public class DataBaseManagerTests {
    private static DatabaseManager db;
    private final UserDataAccess userAccess = new MySqlUserDAO();

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

}
