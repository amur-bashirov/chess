package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
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
import java.util.Collection;
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
               GameData data = gameAccess.getGame2(gameId);
               if (data != null){
                   if(data.whiteUsername().equals(username)){
                       var message = String.format("%s is in the the game as white player", username);
                       var notification = new ServerMessage.notificationMessage(message);
                       connectionManager.broadcast(username, notification);
                   } else if(data.blackUsername().equals(username)){
                       var message = String.format("%s is in the the game as black player", username);
                       var notification = new ServerMessage.notificationMessage(message);
                       connectionManager.broadcast(username, notification);
                   } else {
                       var message = String.format("%s is in the the game as observer", username);
                       var notification = new ServerMessage.notificationMessage(message);
                       connectionManager.broadcast(username, notification);
                   }
               } else {
                   throw new DataAccessException("The game was not found");
               }
           } else{
               throw new DataAccessException("The game was not found");
           }

       }catch (DataAccessException ex){
           sendError(ex, session);
       }catch (IOException ex) {
           ex.printStackTrace();
           sendError(ex, session);
       }
    }
    void makeMove(Session session, String username,String message, int gameId) throws DataAccessException, IOException {
        try {
            Gson serializer = new Gson();
            UserGameCommand.Move move = serializer.fromJson(message, UserGameCommand.Move.class);
            ConnectionManager connectionManager = connectionManagers.get(gameId);
            if (connectionManager != null) {
                GameData data = gameAccess.getGame2(gameId);
                if (data != null) {
                    if (data.whiteUsername().equals(username)) {
                        ChessGame game = data.game();
                        game.makeMove(move.getMove());
                        ChessGame.TeamColor color = game.getTeamTurn();
                        if (game.isInCheckmate(color)){
                            var message2 = String.format("%s player is in the the checkmate", color.toString());
                            var notification = new ServerMessage.notificationMessage(message2);
                            connectionManager.broadcast(username, notification);
                        } else

                    } else {throw new DataAccessException("The game was not found");}
                }
            } else{throw new DataAccessException("The connection was not found");}
        } catch (DataAccessException ex){
            sendError(ex, session);
        } catch (InvalidMoveException ex){
            sendError(ex, session);
        }

    }
    void leave(Session session, String username, int gameId) throws IOException {
       try {
           ConnectionManager connectionManager = connectionManagers.get(gameId);
           if (connectionManager != null) {
               connectionManager.remove(username);
               session.close();
               var message = String.format("%s left the the game", username);
               var notification = new ServerMessage.notificationMessage(message);
               connectionManager.broadcast(username, notification);
           }else{
               throw new DataAccessException("The game was not found");
           }
       } catch (IOException ex){
           ex.printStackTrace();
           sendError(ex, session);
       } catch (DataAccessException ex) {
           sendError(ex, session);
       }
    }
    void resign(Session session, String username, int gameId) throws IOException {
        try{
            ConnectionManager connectionManager = connectionManagers.get(gameId);
            if (connectionManager != null) {

                var message = String.format("%s resigned the the game", username);
                var notification = new ServerMessage.notificationMessage(message);
                connectionManager.broadcast(username, notification);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            sendError(ex, session);
        }
    }
}
