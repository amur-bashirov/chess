package ui;





import websocket.messages.ServerMessage;

import java.util.Scanner;




import static ui.EscapeSequences.*;

public class Repl  implements NotificationHandler{

    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private State state = State.LOGEDOUT;
    private String authToken = "";
    private final ChessClient chessClient;


    public Repl(String serverUrl){
        preloginClient = new PreloginClient(serverUrl);
        postloginClient = new PostloginClient(serverUrl,state,authToken);
        chessClient = new ChessClient(serverUrl, state, authToken);
    }

    public State getState(){
        return this.state;
    }

    public void run() {
        System.out.println( WHITE_KING + "Welcome to 240 chess. Type help to get started" + WHITE_KING);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            try {
                if (state.equals(State.LOGEDOUT)) {
                    result = preloginClient.eval(line);
                    authToken = preloginClient.getAuthToken();
                    state = preloginClient.getState();
                    System.out.print(result);
                }else if (state.equals(State.LOGEDIN)){
                    result = postloginClient.eval(line,state,authToken);
                    state = postloginClient.getState();
                    System.out.print( result);
                }
                else{
                    result = chessClient.eval(line,state, authToken);
                    state = chessClient.getState();
                    System.out.print(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        result ="Have a nice day!;)\n"+
        "Have the best day!\n"+
        "Have the very very best day!";
        System.out.println(result);
        }

    public void notify(ServerMessage.ServerMessageType notification){
        //not sure what it is supposed to do
        System.out.println(SET_TEXT_COLOR_RED + notification.message());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n"  + ">>> " );
    }
}
