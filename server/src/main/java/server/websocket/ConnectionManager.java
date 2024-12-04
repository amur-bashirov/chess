package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String,Connection> connections = new ConcurrentHashMap<>();
    public boolean stopGame = false;
    public void add(String name, Session session) {
        var connection = new Connection(name, session);
        connections.put(name, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public boolean isStopGame() {
        return stopGame;
    }

    public void stopGame(){
        stopGame = true;
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.getName().equals(excludeVisitorName)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }


        for (var c : removeList) {
            connections.remove(c.getName());
        }
    }
}
