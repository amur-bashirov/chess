import chess.*;
import spark.Spark;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var sever = new Server();
        sever.run(8080);
    }
}