package server.websocket;
import javax.websocket.*;
import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;

public class Connection {
    public String whiteName;
    public Session session;

    public Connection(String name, Session session){
        this.whiteName = name;
        this.session = session;
    }

    public String getName() {
        return whiteName;
    }

    public Session getSession() {
        return session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
