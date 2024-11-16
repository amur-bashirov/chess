import server.Server;
import dataaccess.DataAccessException;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var sever = new Server();
        sever.run(8080);
    }
}