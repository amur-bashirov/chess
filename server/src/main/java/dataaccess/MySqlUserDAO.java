package dataaccess;

import static dataaccess.DatabaseManager.createDatabase;
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
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(data);
        executeUpdate(statement, data.username(), data.password(), data.email(), json) ;
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

//    private final String[] createStatements = {
//            """
//            CREATE TABLE IF NOT EXISTS  user (
//              `username` varchar(256) NOT NULL,
//              `password` varchar(256) NOT NULL,
//              `email` varchar(256) NOT NULL,
//              PRIMARY KEY (`username`),
//              INDEX(username),
//            )
//            """
//    };
//
//    private void configureDatabase() throws DataAccessException {
//        DatabaseManager.createDatabase();
//        try (var conn = DatabaseManager.getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException ex) {
//            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p);}
                    else if (param instanceof Integer p) {ps.setInt(i + 1, p);}
                    //else if (param instanceof PetType p) {ps.setString(i + 1, p.toString());}
                    else if (param == null){ ps.setNull(i + 1, NULL);}
                }
                ps.executeUpdate();

//                var rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//
//                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException( String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
    void storeUserPassword(String username, String clearTextPassword) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

        // write the hashed password in database along with the user's other information
        writeHashedPasswordToDatabase(username, hashedPassword);
    }

    private void writeHashedPasswordToDatabase(String username, String hashedPassword) throws DataAccessException {

        String statement = null;
        try (var conn = DatabaseManager.getConnection()) {
            statement = "UPDATE user SET password = ? WHERE username = ?";
            executeUpdate(statement, hashedPassword, username);
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
