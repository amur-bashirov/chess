package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.util.UUID;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySqlAuthDAO implements AuthDataAccess{
    @Override
    public AuthData createAuth(UserData data) {
        AuthData authData = new AuthData(UUID.randomUUID().toString(),data.username());
        String authToken = authData.authToken();
        String username = authData.username();
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try {
            executeUpdate(statement, authToken, username); ;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return authData;
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, token);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String authToken = rs.getString("authToken");
                        String username = rs.getString("username");
                        AuthData data = new AuthData(authToken, username);
                        return data;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, data.authToken());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);

    }
}
