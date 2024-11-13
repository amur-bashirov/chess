import DataObjects.DataAccessException;
import DataObjects.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var sever = new Server();
        sever.run(8080);
    }
}