package server;

import com.google.gson.Gson;
import dataaccess.*;
import service.*;
import spark.*;

import java.util.Map;


public class Server {

    private final AuthDataAccess authAccess = new MemoryAuthDAO();
    private final UserDataAccess userAccess = new MemoryUserDAO();
    private final UserService userService = new UserService(userAccess,authAccess);
    private final GameDataAccess gameAccess = new MemoryGameDAO();
    private final GameService gameService = new GameService(gameAccess, authAccess);
    private final ClearAccess clearAccess = new MemoryClearDAO(authAccess, userAccess, gameAccess);
    private final ClearService clearService = new ClearService(clearAccess);

    //public Server()


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user",this::Register);
        Spark.exception(DataAccessException.class, this::handleDataAccessException);
        Spark.exception(BadRequestsException.class, this::handleBadRequestException);
        Spark.exception(OccupiedException.class, this::handleOccupiedException);
        Spark.exception(Exception.class, this::handleGenericException);
        Spark.post("/session",this::Login);
        Spark.delete("/session",this::Logout);
        Spark.get("/game",this::ListGames);
        Spark.post("/game",this::CreateGame);
        Spark.put("/game",this::JoinGame);
        Spark.delete("/db",this::Clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object ListGames(Request req, Response res) throws DataAccessException{
        String authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }
        ListGamesRequest request = new ListGamesRequest(authToken);
        var serializer = new Gson();
        ListGamesResult result = gameService.listGames(request);var json = serializer.toJson(result);
        res.body(json);
        return json;

    }

    private Object Clear(Request req, Response res) {
        clearService.clear();
        String json = new Gson().toJson(new Object()); // Return empty JSON object
        res.body(json);
        res.status(200); // OK
        return json;
    }

    private Object JoinGame(Request req, Response res) throws DataAccessException{
        var serializer = new Gson();
        String authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }
        JoinGameRequest tempRequest = serializer.fromJson(req.body(),JoinGameRequest.class);
        if (tempRequest.playerColor() == null) {//tempRequest.gameID() == null
            throw new BadRequestsException("bad request");
        }
        JoinGameRequest request = new JoinGameRequest(authToken,tempRequest.playerColor(),tempRequest.gameID());
        if (request.authToken().equals(null) || request.playerColor().equals(null))  {
            throw new BadRequestsException("bad request");
        }
        gameService.joinGame(request);
        String json = new Gson().toJson(new Object()); // Return empty JSON object
        res.body(json);
        res.status(200); // OK
        return json;
    }

    private Object CreateGame(Request req, Response res) throws DataAccessException {
        var serializer = new Gson();
        String authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }
        CreateGamesRequest tempRequest = serializer.fromJson(req.body(),CreateGamesRequest.class);
        if (tempRequest.gameName() == null) {
            throw new BadRequestsException("bad request");
        }
        CreateGamesRequest request = new CreateGamesRequest(authToken,tempRequest.gameName());
        if (request.authToken().equals(null) || request.gameName().equals(null))  {
            throw new BadRequestsException("bad request");
        }
        CreateGameResult result = gameService.createGame(request);
        var json = serializer.toJson(result);
        res.body(json);
        res.status(200);
        return json;
    }



    private Object Logout(Request req, Response res) throws DataAccessException{
        String authToken = req.headers("Authorization");

        // Validate the Authorization token
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }

        LogoutRequest request = new LogoutRequest(authToken);
        userService.logout(request);

        String json = new Gson().toJson(new Object()); // Return empty JSON object
        res.body(json);
        res.status(200); // OK
        return json;

    }

    public Object Login(Request req, Response res) throws DataAccessException{
        var serializer = new Gson();
        LoginRequest request = serializer.fromJson(req.body(),LoginRequest.class);
        LoginResult result = userService.login(request);
        var json = serializer.toJson(result);
        res.body(json);
        return json;
    }

    public Object Register(Request req, Response res) throws DataAccessException {
        var serializer = new Gson();
        RegisterRequest request = serializer.fromJson(req.body(),RegisterRequest.class);
        //if username, email or password is null thro the exception
        if (request.username() == null || request.email() == null || request.password() == null) {
            throw new BadRequestsException("bad request");
        }
        RegisterResult result = userService.register(request);
        var json = serializer.toJson(result);
        res.body(json);
        return json;
    }

    public Object handleDataAccessException(DataAccessException e, Request req, Response res){
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),"success", false));
        res.body(body);
        res.type("application/json");
        res.status(401);
        return body;
    }
    public Object handleGenericException(Exception e, Request req, Response res){
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),"success", false));
        res.body(body);
        res.type("application/json");
        res.status(500);
        return body;
    }

    public Object handleBadRequestException(BadRequestsException e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.body(body);
        res.type("application/json");
        res.status(400); // Set HTTP status to 400 Bad Request
        return body;
    }

    public Object handleOccupiedException(OccupiedException e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.body(body);
        res.type("application/json");
        res.status(403); // Set HTTP status to 400 Bad Request
        return body;
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }



}
