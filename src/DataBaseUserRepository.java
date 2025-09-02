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


public class DataBaseUserRepository implements UserRepository {
    private final DatabaseConnection dbConnection;
    
    public DataBaseUserRepository(DatabaseConnection dbConnFection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Finds a user via their username
     * @param username
     * @return User found
     */
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
                Role role = Role.valueOf(rs.getString("role"));
                return Optional.ofNullable(new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    role
                ));
            }
            return Optional.empty();

        } catch (SQLException ex){
            throw new DataBaseConnectionException("Failed to connect to database", ex);
        } finally {
            closeResources(stmt, rs, conn);
        }
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
            closeResources(stmt, null, conn);
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
            if (rs.next()) return Optional.ofNullable(rs.getString("password"));
        } catch (SQLException ex){
            throw new DataBaseConnectionException("Failed to connect to database", ex);
        } finally {
            closeResources(stmt, rs, conn);
        }
        return Optional.ofNullable(null);
    }

    @Override 
    public void insertUser(User user) throws DataBaseConnectionException{
        String sql = "INSERT OR IGNORE INTO users (username, password, role) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
        } catch(SQLException ex){
            throw new DataBaseConnectionException("Failed to connect to database", ex);
        } finally {
            closeResources(stmt, rs, conn);
        }
    }

    private void closeResources(PreparedStatement stmt, ResultSet rs, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { System.err.println("Error closing connection: " + e.getMessage());}
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) {System.err.println("Error closing connection: " + e.getMessage()); }
        }
        dbConnection.closeConnection(conn);
    }
}