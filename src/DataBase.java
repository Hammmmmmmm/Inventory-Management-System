import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DataBase {
    private static final String URL;

    static {
        String projectRoot = System.getProperty("user.dir");
        String dbPath = projectRoot + "\\lib\\other\\users.db";
        URL = "jdbc:sqlite:" + dbPath;
    }

    /**
     * Creates a live connection to the databases
     * @return
     */
    public static Connection connect() throws Exception{
        return DriverManager.getConnection(URL);
    }

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
    }}

    public static Optional<String> getHashedPassword(String userName) throws SQLException, Exception{
        String sql = "SELECT password FROM user WHERE username = ?";
        Connection conn = connect(); 
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, userName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return Optional.ofNullable(rs.getString("password"));

        return Optional.ofNullable(null);

    }    

}
    


