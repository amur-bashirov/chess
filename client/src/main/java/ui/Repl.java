package ui;





import java.util.Scanner;


import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;
import static java.awt.Color.ORANGE;
import static java.awt.Color.RED;
import static ui.EscapeSequences.WHITE_KING;

public class Repl {
    private final PreloginClient preloginClient;

    public Repl(String serverUrl){
        preloginClient = new PreloginClient(serverUrl);
    }

    public void run() {
        System.out.println(WHITE_KING + "Welcome to 240 chess. Type help to get started" + WHITE_KING);
        System.out.print(preloginClient.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = preloginClient.eval(line);
                System.out.print(ORANGE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + ORANGE);
    }
}
