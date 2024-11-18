package service;

import dataaccess.AuthDataAccess;

import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;
import dataObjects.*;

import java.util.ArrayList;


public class GameService {

    private final GameDataAccess gameMethods;
    private final AuthDataAccess authMethods;

    public GameService(GameDataAccess game, AuthDataAccess auth) {
        this.gameMethods = game;
        this.authMethods = auth;
    }


    public ListGamesResult listGames(ListGamesRequest request) throws dataaccess.DataAccessException {
        AuthData authData = authMethods.getAuth(request.authToken());
        if (authData != null){
            ArrayList<GameData> gameList = gameMethods.listGames();
            ListGamesResult games = new ListGamesResult(gameList);
            return games;
        }
        throw new dataaccess.DataAccessException("unauthorized");
    }

    public CreateGameResult createGame(CreateGamesRequest request) throws DataAccessException{
        AuthData authData = authMethods.getAuth(request.authToken());
        if (authData != null){

                GameData newGame = gameMethods.createGame(request.gameName());
                CreateGameResult result = new CreateGameResult(newGame.gameID());
                return result;

        }
        throw new DataAccessException("unauthorized");
    }


    public void joinGame(JoinGameRequest request) throws dataaccess.DataAccessException, OccupiedException, BadRequestsException {
        AuthData authData = authMethods.getAuth(request.authToken());
        if (authData != null){
            GameData game = gameMethods.getGame2(request.gameID());
            if (game != null){
                gameMethods.updateGame(request.playerColor(),request.gameID(), authData.username());
                return;
            }
            throw new BadRequestsException("bad request");
        }
        throw new dataaccess.DataAccessException("unauthorized");
    }
}
