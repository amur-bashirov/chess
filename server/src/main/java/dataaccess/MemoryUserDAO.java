package dataaccess;

import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO implements UserDataAccess{

    ArrayList<UserData> UsersList = new ArrayList();



    public UserData getUser(String username){
        for (UserData user: UsersList){
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void creatUser(UserData data) {
        UsersList.add(data);
    }
}
