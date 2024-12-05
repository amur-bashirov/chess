import chess.*;

import ui.Repl;
import ui.ResponseException;

public class Main {
    public static void main(String[] args) throws ResponseException {
        var serverUrl = "http://localhost:8080";
        if(args.length > 0) {
            serverUrl = args[0];
        }
        try {
            new Repl(serverUrl).run();
        } catch(ResponseException ex){
            System.out.println(ex);
            System.out.println("Problems with connection");
        }
    }
}