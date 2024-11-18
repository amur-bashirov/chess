package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import dataObjects.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CombinedServiceTests {
    private static RegisterRequest existingUser;

    private static RegisterRequest newUser;






    private String existingAuth;
    private final AuthDataAccess authAccess = new MemoryAuthDAO();
    private final UserDataAccess userAccess = new MemoryUserDAO();
    private final UserService userService = new UserService(userAccess,authAccess);
    private final GameDataAccess gameAccess = new MemoryGameDAO();
    private final GameService gameService = new GameService(gameAccess, authAccess);
    private final ClearAccess clearAccess = new MemoryClearDAO(authAccess, userAccess, gameAccess);
    private final ClearService clearService = new ClearService(clearAccess);
    @BeforeAll
    public static void starter(){
        existingUser = new RegisterRequest("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new RegisterRequest("NewUser", "newUserPassword", "nu@mail.com");


    }
    @BeforeEach
    public void setup() throws OccupiedException, DataAccessException {
        clearService.clear();
        RegisterResult result = userService.register(existingUser);
        existingAuth = result.authToken();
    }

    @Test
    @DisplayName("Successful User Registration")
    public void testSuccessfulRegistration() throws OccupiedException, DataAccessException {
        RegisterResult result = userService.register(newUser);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(newUser.username(), result.username());
        Assertions.assertNotNull(result.authToken());
    }
    @Test
    @DisplayName("User Registration - Username Taken")
    public void testUsernameTaken() {
        Assertions.assertThrows(OccupiedException.class, () -> userService.register(existingUser));
    }

    @Test
    @DisplayName("Successful User Login")
    public void testSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest(existingUser.username(), existingUser.password());
        try {
            LoginResult result = userService.login(loginRequest);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(existingUser.username(), result.username());
            Assertions.assertNotNull(result.authToken());
        } catch (DataAccessException e) {
            Assertions.fail("Login should succeed, but threw dataaccess.DataAccessException: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("User Login - Incorrect Password")
    public void testLoginIncorrectPassword() {
        LoginRequest loginRequest = new LoginRequest(existingUser.username(), "wrongPassword");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    @DisplayName("User Login - Nonexistent User")
    public void testLoginNonexistentUser() {
        LoginRequest loginRequest = new LoginRequest("NonexistentUser", "password");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    @DisplayName("Successful Game Creation")
    public void testCreateGame() {
        CreateGamesRequest request = new CreateGamesRequest(existingAuth, "Test Game");
        try{
            CreateGameResult result = gameService.createGame(request);
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.gameID() > 0);
        } catch (DataAccessException e) {
            Assertions.fail("Game creation should succeed, but threw dataaccess.DataAccessException: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Game Creation - Unauthorized")
    public void testCreateGameUnauthorized() {
        CreateGamesRequest request = new CreateGamesRequest("Unauthorized Game", "invalidToken");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(request));
    }

    @Test
    @Order(8)
    @DisplayName("List Games - No Games Available")
    public void testListNoGames() {
        ListGamesRequest request = new ListGamesRequest(existingAuth);
        try{
            ListGamesResult result = gameService.listGames(request);
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.games().isEmpty());
        } catch (DataAccessException e) {
            Assertions.fail("List games should succeed, but threw dataaccess.DataAccessException: " + e.getMessage());
        }
    }

    @Test
    @Order(9)
    @DisplayName("Clear Data - Success")
    public void testClearData() throws OccupiedException, DataAccessException {
        RegisterResult result = userService.register(newUser);
        existingAuth = result.authToken();
        clearService.clear();
        RegisterResult result2 = userService.register(newUser);
        existingAuth = result.authToken();

        Assertions.assertTrue(true);

    }

    @Test
    @Order(10)
    @DisplayName("Multiple Clear Operations - Success")
    public void testMultipleClears() throws OccupiedException, DataAccessException {
        RegisterResult result = userService.register(newUser);
        existingAuth = result.authToken();
        clearService.clear();
        RegisterResult result2 = userService.register(newUser);
        existingAuth = result.authToken();


        existingAuth = result.authToken();
        clearService.clear();
        RegisterResult result3 = userService.register(newUser);
        existingAuth = result.authToken();
        Assertions.assertTrue(true);
    }
    @Test
    @Order(11)
    @DisplayName("Successful User Logout")
    public void testSuccessfulLogout() {
        LogoutRequest logoutRequest = new LogoutRequest(existingAuth);
        try {
            userService.logout(logoutRequest);
        } catch (DataAccessException e) {
            Assertions.fail("Logout should succeed, but threw dataaccess.DataAccessException: " + e.getMessage());
        }
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(logoutRequest);
        }, "Logging out again with the same auth token should throw dataaccess.DataAccessException");
    }
    @Test
    @Order(12)
    @DisplayName("Logout with Invalid Token")
    public void testLogoutWithInvalidToken() {
        LogoutRequest logoutRequest = new LogoutRequest("invalidToken");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(logoutRequest);
        });
    }
    @Test
    @Order(13)
    @DisplayName("Successful List Games")
    public void testSuccessfulListGames() {
        CreateGamesRequest createRequest = new CreateGamesRequest(existingAuth, "Test Game");
        try {
            CreateGameResult createResult = gameService.createGame(createRequest);
            Assertions.assertNotNull(createResult, "Game creation should return a result");
        } catch (DataAccessException e) {
            Assertions.fail("Game creation should succeed, but threw dataaccess.DataAccessException: " + e.getMessage());
        }

        ListGamesRequest listRequest = new ListGamesRequest(existingAuth);
        try {
            ListGamesResult result = gameService.listGames(listRequest);
            Assertions.assertNotNull(result, "ListGamesResult should not be null");
            Assertions.assertFalse(result.games().isEmpty(), "Game list should not be empty");
        } catch (DataAccessException e) {
            Assertions.fail("Listing games should succeed, but threw dataaccess.DataAccessException: " + e.getMessage());
        }
    }


}


