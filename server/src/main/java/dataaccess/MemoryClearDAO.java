package dataaccess;

public class MemoryClearDAO implements ClearAccess {

    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final GameDataAccess gameAccess;


    public MemoryClearDAO(AuthDataAccess auth, UserDataAccess user, GameDataAccess game) {
        this.authAccess = auth;
        this.userAccess = user;
        this.gameAccess = game;
    }

    @Override
    public void clear() {
        authAccess.clear();
        userAccess.clear();
        gameAccess.clear();
    }
}
