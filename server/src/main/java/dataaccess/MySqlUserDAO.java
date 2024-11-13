package dataaccess;

import static dataaccess.DatabaseManager.executeUpdate;

import DataObjects.DataAccessException;
import model.UserData;

import java.util.ArrayList;

public class MySqlUserDAO implements UserDataAccess{

    private ArrayList<UserData> usersList = new ArrayList();

    public MySqlUserDAO() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }



    @Override
    public UserData getUser(String username) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String newUsername = rs.getString("username");
                        String newPassword  = rs.getString("password");
                        String email = rs.getString("email");
                        UserData newUser = new UserData(newUsername,newPassword, email);
                        return newUser;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void creatUser(UserData data) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, data.username(), data.password(), data.email()) ;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }











}
