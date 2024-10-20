package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDataAccess {
    AuthData createAuth(UserData data);
    AuthData getAuth(String token);
    void deleteAuth(AuthData data);
}
