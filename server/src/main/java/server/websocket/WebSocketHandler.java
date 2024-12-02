package server.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ClearService;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import spark.*;

import java.io.IOException;

public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final UserService userService;
    private final GameDataAccess gameAccess;
    private final GameService gameService ;
    private final ClearAccess clearAccess ;
    private final ClearService clearService ;

    public WebSocketHandler(pass it in here){

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try{
            Gson serializer = new Gson();
            UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
            String user = authAccess.getAuth(command.getAuthToken());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
