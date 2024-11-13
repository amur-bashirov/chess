package service;

import dataaccess.AuthDataAccess;

import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import DataObjects.*;


public class UserService {

    private final UserDataAccess userMethods;
    private final AuthDataAccess authMethods;


    public UserService(UserDataAccess user, AuthDataAccess auth){
        this.userMethods = user;
        this.authMethods = auth;
    }

    public RegisterResult register(RegisterRequest request) throws OccupiedException, DataAccessException {
        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        UserData data = new UserData(request.username(),hashedPassword,request.email());
        UserData data2 = userMethods.getUser(data.username());
        if (data2 == null){
            userMethods.creatUser(data);
            AuthData auth = authMethods.createAuth(data);
            RegisterResult result = new RegisterResult(auth.username(),auth.authToken());
            return result;

        }
        throw new OccupiedException("username already taken");
    }
    public LoginResult login(LoginRequest request) throws DataAccessException{
        UserData data = userMethods.getUser(request.username());
        if(data != null) {
            if (BCrypt.checkpw(request.password(),data.password())) {
                AuthData auth = authMethods.createAuth(data);
                AuthData auth2 = authMethods.getAuth(auth.authToken());
                LoginResult result = new LoginResult(auth.username(), auth.authToken());
                return result;
            }
            throw new DataAccessException("unauthorized");
        }
        throw new DataAccessException("unauthorized");
    }
    public void logout(LogoutRequest request) throws DataAccessException{
        AuthData data = authMethods.getAuth(request.authToken());
        if (data != null){
            authMethods.deleteAuth(data);
            return;
        }
        throw new DataAccessException("unauthorized");
    }
}
