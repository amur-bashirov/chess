package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ClearService;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import spark.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketHandler {
    private Map<Integer, ConnectionManager> connectionManagers = new HashMap<>();
    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final UserService userService;
    private final GameDataAccess gameAccess;
    private final GameService gameService ;
    private final ClearAccess clearAccess ;
    private final ClearService clearService ;

    public WebSocketHandler(AuthDataAccess authAccess, UserDataAccess userAccess, UserService userService,
                            GameDataAccess gameAccess, GameService gameService, ClearAccess clearAccess, ClearService clearService){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.userService = userService;
        this.gameAccess = gameAccess;
        this.gameService = gameService;
        this.clearAccess = clearAccess;
        this.clearService = clearService;

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try{
            Gson serializer = new Gson();
            UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
            AuthData data = authAccess.getAuth(command.getAuthToken());
            if (data == null){
                throw new DataAccessException("Unauthorized access: Invalid auth token");
            } else {
                ConnectionManager connectionManager = new ConnectionManager();
                connectionManager.add(data.username(),session);
                connectionManagers.put(command.getGameID(), new ConnectionManager());
                switch (command.getCommandType()) {
                    case CONNECT -> connect(session, data.username(), command.getGameID());
                    case MAKE_MOVE -> makeMove(session, data.username(),message, command.getGameID());
                    case LEAVE -> leave(session, data.username(), command.getGameID());
                    case RESIGN -> resign(session, data.username(), command.getGameID());
                }
            }
        } catch (DataAccessException ex) {
           sendError(ex, session);
        } catch( Exception ex){
            ex.printStackTrace();
            sendError(ex, session);
        }
    }

    public void sendError(Exception ex, Session session) throws IOException {
        Gson serializer = new Gson();
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        String jsonResponse = serializer.toJson(errorResponse);
        session.getRemote().sendString(jsonResponse);
    }



    void connect(Session session, String username, int gameId) throws IOException {
       try {
           ConnectionManager connectionManager = connectionManagers.get(gameId);
           if (connectionManager != null){
               // do I need to specify that it is observer or player?
               var message = String.format("%s is in the the game", username);
               var notification = new ServerMessage.notificationMessage(message);
               connectionManager.broadcast(username, notification);
           } else{
               throw new DataAccessException("The game was not found");
           }

       } catch (IOException ex){
           ex.printStackTrace();
           sendError(ex, session);
       } catch (DataAccessException ex){
           sendError(ex, session);
       }
    }
    void makeMove(Session session, String username,String message, int gameId){
        Gson serializer = new Gson();
        UserGameCommand.Move move = serializer.fromJson(message, UserGameCommand.Move.class);
        connections.add(username, session);

    }
    void leave(Session session, String username, int gameId) throws IOException {
       try {
           connections.remove(username);
           var message = String.format("%s left the the game", username);
           var notification = new ServerMessage.notificationMessage(message);
           connections.broadcast(username, notification);
           //do I need to logout? or update the game?
       } catch (IOException ex){
           ex.printStackTrace();
           sendError(ex, session);
       }
    }
    void resign(Session session, String username, int gameId){

    }
}
