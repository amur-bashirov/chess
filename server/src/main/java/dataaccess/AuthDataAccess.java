package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDataAccess {
    AuthData createAuth(UserData data) throws DataAccessException;
    AuthData getAuth(String token) throws DataAccessException;
    void deleteAuth(AuthData data) throws DataAccessException;
    void clear() throws DataAccessException;

}
