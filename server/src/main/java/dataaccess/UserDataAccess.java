package dataaccess;

import model.UserData;


public interface UserDataAccess {
    UserData getUser(String username) throws dataaccess.DataAccessException;
    void creatUser(UserData data) throws dataaccess.DataAccessException;
    void clear() throws DataAccessException;
}
