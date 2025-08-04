import java.lang.foreign.Linker.Option;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.DataBaseConnectionException;
import Exceptions.InsufficientPermissionsException;
import Exceptions.UserNotLoggedInException;

interface DatabaseConnection {
    Connection getConnection() throws DataBaseConnectionException;
    void closeConnection(Connection conn);
}

class SQLiteDataBaseConnection implements DatabaseConnection {
    //private static final String URL = "jdbc:sqlite:lib/other/users.db";
    private final String url;
    
    public SQLiteDataBaseConnection(String url) {
        this.url = url;
    }
    /**
     * Creates a live connection to the databases
     * @return a valid SQL Connection
     * @throws DataBaseConnectionException if the connection fails
     */
    @Override
    public Connection getConnection() throws DataBaseConnectionException {
        try {
            Class.forName("org.sqlite.JDBC"); // Checks if driver present
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException ex) {
            throw new DataBaseConnectionException("SQLite JDBC driver not found", ex);
        
        } catch (Exception ex) {
            throw new DataBaseConnectionException("Failed to connect to DB at: " + url, ex);
        }
    }

    @Override
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}

interface UserRepository {
    void createUsersTable() throws Exception;
    Optional<String> getHashedPassword(String userName) throws SQLException, DataBaseConnectionException;
    void insertUser(User user) throws DataBaseConnectionException, InsufficientPermissionsException, UserNotLoggedInException;
    Optional<User> findByUsername(String username) throws DataBaseConnectionException;
    boolean userExists(String username) throws DataBaseConnectionException;
}


interface userService {
    void createUser(User user) throws InsufficientPermissionsException, UserNotLoggedInException, DataBaseConnectionException;
    boolean authenticateUser(String username, String password) throws DataBaseConnectionException;
}

class DataBaseUserRepository implements UserRepository {
    private final DatabaseConnection dbConnection;
    
    public DataBaseUserRepository(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<User> findByUsername(String username) throws DataBaseConnectionException {
        String sql = "SELECT username, password, role FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                User.Role role = User.Role.valueOf(rs.getString("role"));
                return Optional.of(new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    role
                ));
            }
            return Optional.empty();

        } catch (SQLException ex){
            throw new DataBaseConnectionException("Failed to connect to database", ex);
        } finally {
            // TODO release resources
        }

        
        if (rs.next()) return Optional.ofNullable(rs.getString("password"));

        return Optional.ofNullable(null);
    }

    /**
     * Initially creates the UserLoginTable if its not
     * already present
     * @throws Exception
     */
    @Override
    public void createUsersTable() throws DataBaseConnectionException{
        String sql = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT, role TEXT)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.execute();
        } catch (SQLException ex) {
            throw new DataBaseConnectionException("Failed to create users table", ex);
        } finally {
            //TODO close database connection and release resources
        }
    }

    @Override 
    public boolean userExists(String username) throws DataBaseConnectionException{
        return findByUsername(username).isPresent();
    }

    /**
     * Gets the Password Hash of a given user by their Username
     * @param username The Username of the user of which Hash is to be returned
     * @return Optional containing the Hashed Password 
     * @throws SQLException 
     * @throws DataBaseConnectionException 
     */
    @Override 
    public Optional<String> getHashedPassword(String username) throws DataBaseConnectionException{
        String sql = "SELECT password FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
        } catch (SQLException ex){
            throw new DataBaseConnectionException("Failed to connect to database", ex);
        } finally {
            // TODO release resources
        }

        
        if (rs.next()) return Optional.ofNullable(rs.getString("password"));

        return Optional.ofNullable(null);
    }

    @Override 
    public void insertUser(User user) throws DataBaseConnectionException{
        String sql = "INSERT OR IGNORE INTO users (username, password, role) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            st
        }
    }

    private void closeResources(PreparedStatement stmt, ResultSet rs, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { /* TODO log error */ }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { /* TODO log error */ }
        }
        dbConnection.closeConnection(conn);

    }
}


public class DataBase {
    
    

    
    
    public static Optional<String> getHashedPassword(String userName) throws SQLException, DataBaseConnectionException{
        String sql = "SELECT password FROM users WHERE username = ?";
        Connection conn = getConnection(); 
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
    public final static void insertUser(User user) throws DataBaseConnectionException, InsufficientPermissionsException, UserNotLoggedInException{
        if (!LoginScreen.getCurrentUser().isPresent()) throw new UserNotLoggedInException("No user has been logged in? (Should be unreachable)");
        if (LoginScreen.getCurrentUser().get().getUserRole() != User.Role.ADMIN) 
            {
            throw new InsufficientPermissionsException(
                "Attempting to insert new User with insufficient permissions," +
                "Your permission level was " + user.getUserRole().name()
            );
        }
        User currentLoggedInUser = LoginScreen.getCurrentUser().get();
        Connection conn = connect();
        try {
            String sql = "INSERT OR IGNORE INTO users (username, password, role) VALUES (?, ?, ?)";    
            PreparedStatement stmt = conn.prepareStatement(sql);
        
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getPasswordHash());
            stmt.executeUpdate();
        } catch (SQLException e){
            //TODO propigate more appropriate error
        }
    }

    

}
    


