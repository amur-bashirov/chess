package dataaccess;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySqlClear implements ClearAccess {
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user; TRUNCATE game; TRUNCATE auth";
        executeUpdate(statement);
    }
}
