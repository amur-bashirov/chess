package dataaccess;

import DataObjects.DataAccessException;
import model.UserData;


public interface UserDataAccess {
    UserData getUser(String username) throws DataAccessException;
    void creatUser(UserData data) throws DataAccessException;
    void clear() throws DataAccessException;
}
