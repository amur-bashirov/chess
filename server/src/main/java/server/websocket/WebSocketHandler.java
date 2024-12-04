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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();

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
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        String jsonResponse = serializer.toJson(errorResponse);
        session.getRemote().sendString(jsonResponse);
    }

    public void send(GameData data, Session session, String message, String username,
                     ConnectionManager connectionManager) throws IOException {
        var notification = new ServerMessage.notificationMessage(message);
        ServerMessage.LoadGameMessage game = new ServerMessage.LoadGameMessage(data.game());
        session.getRemote().sendString(new Gson().toJson(game));
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
                   send(data,session,message,username,connectionManager);


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
                if (connectionManager.isStopGame()) {
                    String stopMessage = "The game is stopped.";
                    send(data, session, stopMessage, username, connectionManager); // Sending the stop message
                    return; // Exit early since the game is stopped
                }

                if (data != null) {
                    if (data.whiteUsername().equals(username)) {
                        ChessGame game = data.game();
                        game.makeMove(move.getMove());
                        ChessGame.TeamColor color = game.getTeamTurn();
                        gameAccess.updateGame2(gameId,game);
                        String message2 = "";
                        if (game.isInCheckmate(color)){
                            String stopMessage = String.format("%s player is in the the checkmate the game is stopped", color.toString());
                            send(data, session, stopMessage, username, connectionManager);// Sending the stop message
                            connectionManager.stopGame();
                            return;
                        } else if(game.isInCheck(color)){
                            message2 = String.format("%s player's king is in the the check", color.toString());
                        } else if(game.isInStalemate(color)){
                            message2 = String.format(" the game is in stalemate");
                        } else{
                            message2 = String.format("%s player is in the the checkmate", color.toString());
                        }
                        send(data,session,message2,username,connectionManager);



                    } else {throw new DataAccessException("The game was not found");}
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

            if (connectionManager != null) {
                connectionManager.stopGame();
                var message = String.format("%s resigned from the game", username);
                var notification = new ServerMessage.notificationMessage(message);
                connectionManager.broadcast(username, notification);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            sendError(ex, session);
        }
    }
}
