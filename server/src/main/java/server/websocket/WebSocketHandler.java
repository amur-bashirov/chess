package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final Map<Integer, Boolean> stops = new ConcurrentHashMap<>();

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




    // Get the value or default to false
    public boolean getStopStatus(Integer key) {
        return stops.getOrDefault(key, false);
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
                Integer boxedGameId = command.getGameID();
                connectionManager.add(data.username(),session,boxedGameId);
                switch (command.getCommandType()) {
                    case CONNECT -> connect(session, data.username(), boxedGameId);
                    case MAKE_MOVE -> makeMove(session, data.username(),message, boxedGameId);
                    case LEAVE -> leave(session, data.username(), boxedGameId);
                    case RESIGN -> resign(session, data.username(), boxedGameId);
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
        ServerMessage.ErrorMessage error = new ServerMessage.ErrorMessage(ex.getMessage());
        System.out.println(ex.getMessage());
        String jsonResponse = serializer.toJson(error);
        session.getRemote().sendString(jsonResponse);
    }

    public void sendNotification(GameData data, Session session, String message, String username,
                     ConnectionManager connectionManager) throws IOException {
        var notification = new ServerMessage.notificationMessage(message);
        connectionManager.broadcast(username, notification);

    }



    void connect(Session session, String username, Integer gameId) throws IOException {
       try {


           if (connectionManager != null){
               GameData data = gameAccess.getGame2(gameId);
               if (data != null){
                   String message = "";
                   if(data.whiteUsername().equals(username)){
                       message = String.format("%s is in the game as white player", username);
                   } else if(data.blackUsername().equals(username)){
                       message = String.format("%s is in the game as black player", username);
                   } else {
                       message = String.format("%s is in the game as observer", username);
                   }
                   sendNotification(data,session,message,username,connectionManager);
                   ServerMessage.LoadGameMessage game = new ServerMessage.LoadGameMessage(data.game());
                   String jsonString = new Gson().toJson(game);
                   System.out.println(jsonString);
                   session.getRemote().sendString(jsonString);


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

    void makeMove(Session session, String username,String message, Integer gameId) throws DataAccessException, IOException {
        try {

            Gson serializer = new Gson();
            UserGameCommand.Move move = serializer.fromJson(message, UserGameCommand.Move.class);


            if (connectionManager != null) {
                GameData data = gameAccess.getGame2(gameId);
                if (getStopStatus(gameId)) {
                    String stopMessage = "The game is stopped.";
                    sendNotification(data, session, stopMessage, username, connectionManager); // Sending the stop message
                    return; // Exit early since the game is stopped
                }

                if (data != null) {
                    if (data.whiteUsername().equals(username) || data.blackUsername().equals(username)) {
                        ChessGame game = data.game();
                        ChessGame.TeamColor color = game.getTeamTurn();
                        if (data.blackUsername().equals(username) && !color.equals(ChessGame.TeamColor.BLACK)){
                            throw new DataAccessException("The chess piece belongs to white player");
                        }
                        if (data.whiteUsername().equals(username) && !color.equals(ChessGame.TeamColor.WHITE)){
                            throw new DataAccessException("The chess piece belongs to black player");
                        }
                        color = game.getTeamTurn();
                        game.makeMove(move.getMove());
                        gameAccess.updateGame2(gameId,game);
                        String message2 = "";
                        if (game.isInCheckmate(color)){
                            message2 = String.format("%s player is in the the checkmate, the game is stopped", color.toString());
                            stops.put(gameId,true);
                        } else if(game.isInCheck(color)){
                            message2 = String.format("%s player's king is in the the check", color.toString());
                        } else if(game.isInStalemate(color)){
                            message2 = String.format(" the game is in stalemate");
                            stops.put(gameId, true);
                        } else{
                            message2 = String.format("%s player moved piece at %s to %s", color.toString().toLowerCase(),
                                    move.getMove().getStartPosition().toString(),move.getMove().getEndPosition().toString());
                        }
                        sendNotification(data,session,message2,username,connectionManager);
                        ServerMessage.LoadGameMessage game2 = new ServerMessage.LoadGameMessage(game);
                        connectionManager.sendAll(game2);



                    } else {throw new DataAccessException("Only players can move the chess pieces");}
                }
            } else{throw new DataAccessException("The connection was not found");}


        } catch (DataAccessException ex){
            sendError(ex, session);
        } catch (InvalidMoveException ex){
            sendError(ex, session);
        }

    }
    void leave(Session session, String username, Integer gameId) throws IOException {
       try {

           if (connectionManager != null) {
               connectionManager.remove(username, gameId);
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
    void resign(Session session, String username, Integer gameId) throws IOException {
        try{
            GameData data = gameAccess.getGame2(gameId);
            if(getStopStatus(gameId)){
                throw new DataAccessException("One of the player already resigned from the game");
            }

            if (connectionManager != null) {
                if (data != null) {
                    if (data.whiteUsername().equals(username) || data.blackUsername().equals(username)) {
                        stops.put(gameId, true);
                        var message = String.format("%s resigned from the game", username);
                        var notification = new ServerMessage.notificationMessage(message);
                        connectionManager.broadcast(username, notification);
                    }else {throw new DataAccessException("Only players can move the chess pieces");}
                }
            }else{throw new DataAccessException("The game was not found");}
        } catch (IOException ex) {
            ex.printStackTrace();
            sendError(ex, session);
        } catch (DataAccessException ex) {
            sendError(ex, session);
        }
    }
}
