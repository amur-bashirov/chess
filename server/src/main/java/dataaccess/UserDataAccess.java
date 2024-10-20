package dataaccess;

import model.UserData;


public interface UserDataAccess {
    UserData getUser(String username);
    void creatUser(UserData data);
}
