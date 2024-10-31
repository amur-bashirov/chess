import chess.*;
import dataaccess.DataAccessException;
import spark.Spark;
import server.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var sever = new Server();
        sever.run(8080);
    }
}