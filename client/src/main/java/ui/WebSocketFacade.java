package ui;
import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler ) throws ResponseException{
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage notification =
                                new Gson().fromJson(message, ServerMessage.class);
                       switch( notification.getServerMessageType()){
                           case LOAD_GAME: {
                               ServerMessage.LoadGameMessage game =
                                       new Gson().fromJson(message, ServerMessage.LoadGameMessage.class);
                               notificationHandler.notify(game);
                               break;
                           }
                           case ERROR: {
                               ServerMessage.ErrorMessage error =
                                       new Gson().fromJson(message, ServerMessage.ErrorMessage.class);
                               notificationHandler.notify(error);
                               break;
                           }
                           case NOTIFICATION:{
                               ServerMessage.NotificationMessage not =
                                       new Gson().fromJson(message, ServerMessage.NotificationMessage.class);
                               notificationHandler.notify(not);
                               break;
                           }
                       }
                    }catch(Exception ex){
                        System.out.print(ex.getMessage());
                    }
                }
            });
        } catch(DeploymentException | IOException | URISyntaxException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }


    public void join( String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println("Problems with WebSocket");
        }
    }

    public void resign( String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println("Problems with WebSocket");
        }
    }

    public void leave( String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println("Problems with WebSocket");
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) throws ResponseException{
        try {
            UserGameCommand command = new UserGameCommand.Move(UserGameCommand.CommandType.MAKE_MOVE,
                    authToken, gameID, move);
            var action = command;
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println("Problems with WebSocket");
        }

    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){
    }
}
