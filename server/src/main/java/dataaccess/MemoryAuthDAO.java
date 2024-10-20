package dataaccess;

import model.AuthData;
import java.util.ArrayList;
import java.util.UUID;
import model.UserData;

public class MemoryAuthDAO implements AuthDataAccess{

    private int authToken = 0;
    private final ArrayList<AuthData> authList = new ArrayList();

    public AuthData createAuth(UserData data){
        AuthData authData = new AuthData(UUID.randomUUID().toString(),data.username());
        authList.add(authData);
        return authData;
    }
}
