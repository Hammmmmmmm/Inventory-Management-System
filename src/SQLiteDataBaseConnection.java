import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Exceptions.DataBaseConnectionException;

interface DatabaseConnection {
    Connection getConnection() throws DataBaseConnectionException;
    void closeConnection(Connection conn);
}

public class SQLiteDataBaseConnection implements DatabaseConnection {
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