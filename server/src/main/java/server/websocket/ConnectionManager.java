package server.websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager {

    private final Map<Integer, List<Connection>> connections = new ConcurrentHashMap<>();

    public void add(String name, Session session, Integer gameId) {
        var connection = new Connection(name, session);

        // Ensure the gameId exists in the map
        connections.putIfAbsent(gameId, new CopyOnWriteArrayList<>());

        // Iterate through the list and check for duplicates
        var connectionsList = connections.get(gameId);
        boolean exists = false;
        for (Connection conn : connectionsList) {
            if (conn.getName().equals(name)) {
                remove(name,gameId);
                break;
            }
        }

        // Add only if the connection doesn't already exist
        if (!exists) {
            connectionsList.add(connection);
        }
    }


    public void remove(String visitorName, Integer gameId) {
        // Retrieve the list of connections for the specified gameId
        List<Connection> gameConnections = connections.get(gameId);

        if (gameConnections != null) {
            // Use removeIf to remove the specific connection with the matching name
            gameConnections.removeIf(connection -> connection.getName().equals(visitorName));

            // If the list is empty after removal, you can optionally remove the entry for the gameId
            if (gameConnections.isEmpty()) {
                connections.remove(gameId);
            }
        }
    }


    public void broadcast(String excludeVisitorName, ServerMessage notification, Integer game) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Map.Entry<Integer, List<Connection>> entry : connections.entrySet()) {
            Integer gameId = entry.getKey(); // This is the key (gameId)
            List<Connection> connectionList = entry.getValue();// This is the value (List<Connection>)
            for (Connection connection : connectionList){
                if(connection.session.isOpen()){
                    if(!connection.getName().equals(excludeVisitorName) && Objects.equals(game, gameId)){
                        connection.send(new Gson().toJson(notification));
                        System.out.println(new Gson().toJson(notification));
                    }
                }else{
                    removeList.add(connection);
                }
            }
            for (var connection : removeList){
                remove(connection.getName(), gameId);
            }
        }
    }


    public void sendAll( ServerMessage notification, Integer game) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Map.Entry<Integer, List<Connection>> entry : connections.entrySet()) {
            Integer gameId = entry.getKey(); // This is the key (gameId)
            List<Connection> connectionList = entry.getValue();// This is the value (List<Connection>)
            for (Connection connection : connectionList){
                if(connection.session.isOpen() && Objects.equals(game, gameId)){
                    connection.send(new Gson().toJson(notification));
                    System.out.println(new Gson().toJson(notification));
                }else if (!connection.session.isOpen()){
                    removeList.add(connection);
                }
            }
            for (var connection : removeList){
                remove(connection.getName(), gameId);
            }
        }
    }
}
