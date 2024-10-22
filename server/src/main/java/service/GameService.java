package service;

import dataaccess.AuthDataAccess;

import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.*;

import java.util.ArrayList;


public class GameService {

    private final GameDataAccess gameMethods;
    private final AuthDataAccess authMethods;

    public GameService(GameDataAccess game, AuthDataAccess auth) {
        this.gameMethods = game;
        this.authMethods = auth;
    }


    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException{
        AuthData authData = authMethods.getAuth(request.authToken());
        if (authData != null){
            ArrayList<GameData> gameList = gameMethods.listGames();
            ListGamesResult games = new ListGamesResult(gameList);
            return games;
        }
        throw new DataAccessException("unauthorized");
    }

    public CreateGameResult createGame(CreateGamesRequest request) throws DataAccessException{
        AuthData authData = authMethods.getAuth(request.authToken());
        if (authData != null){
            GameData game = gameMethods.getGame(request.gameName());
            if (game == null){
                gameMethods.createGame(request.gameName());
                GameData newGame = gameMethods.getGame(request.gameName());
                CreateGameResult result = new CreateGameResult(newGame.gameID());
                return result;
            }
        }
        throw new DataAccessException("unauthorized");
    }


    public void joinGame(JoinGameRequest request) throws DataAccessException{
        AuthData authData = authMethods.getAuth(request.authToken());
        if (authData != null){
            GameData game = gameMethods.getGame2(request.gameID());
            if (game != null){
                gameMethods.updateGame(request.playerColor(),request.gameID(), authData.username());
                return;
            }
        }
        throw new DataAccessException("unauthorized");
    }
}
