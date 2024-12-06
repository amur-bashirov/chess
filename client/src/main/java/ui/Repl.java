package ui;





import chess.ChessGame;
import websocket.messages.ServerMessage;

import java.util.Scanner;




import static ui.EscapeSequences.*;

public class Repl  implements NotificationHandler{

    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private State state = State.LOGEDOUT;
    private String authToken = "";
    private ChessClient chessClient;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private final String serverUrl;
    private String color;
    private ChessGame game;
    private int id = 0;


    public Repl(String serverUrl) throws ResponseException {
        this.serverUrl = serverUrl;
        preloginClient = new PreloginClient(serverUrl);
        postloginClient = new PostloginClient(serverUrl,state,authToken, this);
        chessClient = new ChessClient(serverUrl, state, authToken, ws, color, game,id, this);
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
                    this.ws = postloginClient.getWs();
                    if ( ws != null){
                        this.game = postloginClient.getGame();
                        this.ws = postloginClient.getWs();
                        this.color = postloginClient.getColor();
                        this.id = postloginClient.getId();
                        this.chessClient = new ChessClient(serverUrl, state,
                                authToken, ws,color, game, id, this);
                    }
                }
                else{
                    try {
                        result = chessClient.eval(line, state, authToken, color, game);
                        state = chessClient.getState();
                        game = chessClient.getGame();
                        System.out.print(result);
                    } catch (ResponseException ex){
                        System.out.println(ex.getMessage());
                    }
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

    public void notify(ServerMessage notification) throws ResponseException {
        //not sure what it is supposed to do
        String message = "";
        if(notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR)
        {
            message = ((ServerMessage.ErrorMessage) notification).getErrorMessage();
            System.out.println(SET_TEXT_COLOR_RED + message);
            printPrompt();
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            ChessGame game = ((ServerMessage.LoadGameMessage)notification).getGame();
            this.game = game;
            DrawChessBoard.draw(game,null,color);
            System.out.print("\n"  + ">>> " );

        } else{
            message = ((ServerMessage.NotificationMessage) notification).getMessage();
            System.out.println(SET_TEXT_COLOR_RED);
            throw new ResponseException(415,message);
        }
    }

    private void printPrompt() {
        System.out.print("\n"  + ">>> " );
    }
}
