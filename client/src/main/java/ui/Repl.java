package ui;





import java.util.Scanner;


import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;

import static ui.EscapeSequences.*;

public class Repl {

    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private State state = State.LOGEDOUT;
    private String authToken = "";


    public Repl(String serverUrl){
        preloginClient = new PreloginClient(serverUrl);
        postloginClient = new PostloginClient(serverUrl,state,authToken);
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
                }else {
                    result = postloginClient.eval(line,state,authToken);
                    state = postloginClient.getState();
                    System.out.print( result);
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

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " );
    }
}
