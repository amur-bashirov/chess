package dataaccess;

import static dataaccess.DatabaseManager.createDatabase;
import static dataaccess.DatabaseManager.executeUpdate;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?, ?)";
        executeUpdate(statement, data.username(), data.password(), data.email()) ;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        var UserData = new Gson().fromJson(json, UserData.class);
        return UserData;
    }





    void storeUserPassword(String username, String clearTextPassword) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

        // write the hashed password in database along with the user's other information
        //writeHashedPasswordToDatabase(username, hashedPassword);
    }


}
