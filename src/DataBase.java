import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import Exceptions.DataBaseConnectionException;

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
     */
    public static void createUsersTable() throws Exception{
        String sql = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)";
        Connection conn = connect(); 
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.execute();
        
    }

    public static Optional<String> getHashedPassword(String userName) throws SQLException, Exception{
        String sql = "SELECT password FROM users WHERE username = ?";
        Connection conn = connect(); 
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, userName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return Optional.ofNullable(rs.getString("password"));

        return Optional.ofNullable(null);
    }
    
    public static void insertUser(User user) {
        //TODO Insert user into database Then by default add Admin as a permanent user
    }

}
    


