import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataBase {
    private static final String URL = "jdbc:sqlite:users.db";
    /**
     * Creates a live connection to the databases
     * @return
     */
    public static Connection connect() {
        try {
        return DriverManager.getConnection(URL);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }}
    /**
     * Initially creates the UserLoginTable if its not
     * already present
     */
    public static void createUsersTable() {
    String sql = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)";
    try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.execute();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
    


