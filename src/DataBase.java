import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.DataBaseConnectionException;
import Exceptions.InsufficientPermissionsException;
import Exceptions.UserNotLoggedInException;

public class DataBase {
    private static final String URL = "jdbc:sqlite:lib/other/users.db";
    /**
     * Creates a live connection to the databases
     * @return a valid SQL Connection
     * @throws DataBaseConnectionException if the connection fails
     */
    public static Connection connect() throws DataBaseConnectionException{
        try {
            Class.forName("org.sqlite.JDBC"); // Checks if driver present
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException ex) {
            throw new DataBaseConnectionException("SQLite JDBC driver not found", ex);
        
        } catch (Exception ex) {
            throw new DataBaseConnectionException("Failed to connect to DB at: " + URL, ex);
        }
        
    }

    /**
     * Initially creates the UserLoginTable if its not
     * already present
     * @throws Exception
     */
    public static void createUsersTable() throws Exception{
        String sql = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT, role TEXT)";
        Connection conn = connect(); 
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.execute();
        
    }
    /**
     * Gets the Password Hash of a given user by their Username
     * @param userName The Username of the user of which Hash is to be returned
     * @return Optional containing the Hashed Password 
     * @throws SQLException 
     * @throws DataBaseConnectionException 
     */
    public static Optional<String> getHashedPassword(String userName) throws SQLException, DataBaseConnectionException{
        String sql = "SELECT password FROM users WHERE username = ?";
        Connection conn = connect(); 
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, userName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return Optional.ofNullable(rs.getString("password"));

        return Optional.ofNullable(null);
    }
    /**
     * Inserts a new User into the database
     * Current logged in user must be an ADMIN
     * @param user The new User to be added
     * @throws UserNotLoggedInException
     * @throws InsufficientPermissionsException 
     */
    public final static void insertUser(User user) throws InsufficientPermissionsException, UserNotLoggedInException{
        if (!Authorizer.getCurrentUser().isPresent()) throw new UserNotLoggedInException("No user has been logged in? (Should be unreachable)");
        if (Authorizer.getCurrentUser().get().getUserRole() != User.Role.ADMIN) 
            {
            throw new InsufficientPermissionsException(
                "Attempting to insert new User with insufficient permissions," +
                "Your permission level was " + user.getUserRole().name()
            );
        }
        User currentLoggedInUser = Authorizer.getCurrentUser().get();
        String sql = "INSERT OR IGNORE INTO users (username, password, role) VALUES (?, ?, ?)";
        Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement(sql)
        
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPasswordHash());

        

        stmt.setString(3, );
        stmt.executeUpdate();
    }

    /**
     * Checks Inputted User against DataBase
     * @param inputtedUser
     * @return True if Credentials are correct
     * @throws MultipleUserLoginException
     * @throws SQLException
     * @throws DataBaseConnectionException
     */
    public static boolean checkLoginCredentials(String username, String password) throws SQLException, DataBaseConnectionException{
        Optional<String> hashedPassword = DataBase.getHashedPassword(username);
        if (hashedPassword.isPresent() && 
            BCrypt.checkpw(username, hashedPassword.get())) {
                return true; // Credentials correct
        } 
        
        return false;
    }

}
    


