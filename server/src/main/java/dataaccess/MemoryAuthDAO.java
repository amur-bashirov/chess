package dataaccess;

import model.AuthData;
import java.util.ArrayList;
import java.util.Iterator;
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

    public AuthData getAuth(String token){
        for (AuthData data: authList){
            if (data.authToken().equals(token)){
                return data;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) {
        System.out.println("Size of the original ArrayList: " + authList.size());

        Iterator<AuthData> iterator = authList.iterator();
        while (iterator.hasNext()) {
            AuthData authData = iterator.next();
            if (authData.equals(data)) {
                iterator.remove(); // Safe removal
            }
        }
        System.out.println("Size of the ArrayList after deleteAuth method: " + authList.size());
    }
}
