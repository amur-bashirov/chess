package dataaccess;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySqlClear implements ClearAccess {
    @Override
    public void clear() throws DataAccessException {
        executeUpdate("TRUNCATE TABLE user");
        executeUpdate("TRUNCATE TABLE game");
        executeUpdate("TRUNCATE TABLE auth");
    }
}
