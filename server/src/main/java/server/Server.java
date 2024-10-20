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


        Spark.awaitInitialization();
        return Spark.port();
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
