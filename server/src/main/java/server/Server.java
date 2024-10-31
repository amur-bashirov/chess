package server;

import com.google.gson.Gson;
import dataaccess.*;
import service.*;
import spark.*;

import java.util.Map;


public class Server {

    private final AuthDataAccess authAccess = new MemoryAuthDAO();
    private final UserDataAccess userAccess;
    private final UserService userService;
    private final GameDataAccess gameAccess;
    private final GameService gameService ;
    private final ClearAccess clearAccess ;
    private final ClearService clearService ;

    public Server() {
        try {
            this.userAccess = new MySqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        userService = new UserService(userAccess,authAccess);
        gameAccess = new MemoryGameDAO();
         gameService = new GameService(gameAccess, authAccess);
         clearAccess = new MemoryClearDAO(authAccess, userAccess, gameAccess);
         clearService = new ClearService(clearAccess);
    }

    //public Server()


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user",this::register);
        Spark.exception(DataAccessException.class, this::handleDataAccessException);
        Spark.exception(BadRequestsException.class, this::handleBadRequestException);
        Spark.exception(OccupiedException.class, this::handleOccupiedException);
        Spark.exception(Exception.class, this::handleGenericException);
        Spark.post("/session",this::login);
        Spark.delete("/session",this::logout);
        Spark.get("/game",this::listGames);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);
        Spark.delete("/db",this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object listGames(Request req, Response res) throws DataAccessException{
        String authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("unauthorized");
        }
        ListGamesRequest request = new ListGamesRequest(authToken);
        var serializer = new Gson();
        ListGamesResult result = gameService.listGames(request);var json = serializer.toJson(result);
        res.body(json);
        res.status(200);
        return json;

    }

    private Object clear(Request req, Response res) throws DataAccessException {
        clearService.clear();
        String json = new Gson().toJson(new Object()); // Return empty JSON object
        res.body(json);
        res.status(200); // OK
        return json;
    }

    private Object joinGame(Request req, Response res) throws DataAccessException, OccupiedException, BadRequestsException {
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

    private Object createGame(Request req, Response res) throws DataAccessException, BadRequestsException {
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



    private Object logout(Request req, Response res) throws DataAccessException{
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

    public Object login(Request req, Response res) throws DataAccessException{
        var serializer = new Gson();
        LoginRequest request = serializer.fromJson(req.body(),LoginRequest.class);
        LoginResult result = userService.login(request);
        var json = serializer.toJson(result);
        res.body(json);
        res.status(200);
        return json;
    }

    public Object register(Request req, Response res) throws DataAccessException, OccupiedException, BadRequestsException {
        var serializer = new Gson();
        RegisterRequest request = serializer.fromJson(req.body(),RegisterRequest.class);
        //if username, email or password is null thro the exception
        if (request.username() == null || request.email() == null || request.password() == null) {
            throw new BadRequestsException("bad request");
        }
        RegisterResult result = userService.register(request);
        var json = serializer.toJson(result);
        res.body(json);
        res.status(200);
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
