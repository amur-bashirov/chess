package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySqlAuthDAO implements AuthDataAccess{


    @Override
    public AuthData createAuth(UserData data) throws DataAccessException {
        if (data == null){
            return null;
        }
        String authToken = UUID.randomUUID().toString();
        String username = data.username();

        try (var conn = DatabaseManager.getConnection()) {


            var insertStatement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (var ps = conn.prepareStatement(insertStatement)) {
                ps.setString(1, authToken);
                ps.setString(2, username);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database error while creating AuthData for username: " + username);
        }


        return new AuthData(authToken, username);
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
