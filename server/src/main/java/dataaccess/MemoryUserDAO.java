package dataaccess;

import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO implements UserDataAccess{

    private ArrayList<UserData> usersList = new ArrayList();



    public UserData getUser(String username){
        for (UserData user: usersList){
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void creatUser(UserData data) {
        usersList.add(data);
    }

    @Override
    public void clear() {
        usersList.clear();
    }
}
