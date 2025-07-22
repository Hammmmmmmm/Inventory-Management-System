import java.sql.SQLException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.DataBaseConnectionException;

public final class Authorizer {
    public enum LoginResult {
        SUCCESS,
        USER_NOT_FOUND,
        WRONG_PASSWORD
    }

    private static Optional<User> currentUser;

    public static Optional<User> getCurrentUser() {return currentUser;}

    /**
     * Handles Login logic
     * @param user User object, only uses username and passwordHash
     * @throws MultipleUserLoginException
     */
    public static LoginResult tryLogin(User user) throws SQLException, DataBaseConnectionException{
        LoginResult result = checkLoginCredentials(user);
        if (result == LoginResult.SUCCESS) login(user);

        return result;

    }

    private static void login(User user) {
        
            
    }

    /**
     * Checks Inputted User against DataBase
     * @param inputtedUser
     * @return True if Credentials are correct 
     * @throws MultipleUserLoginException
     * @throws SQLException
     * @throws DataBaseConnectionException
     */
    private static LoginResult checkLoginCredentials(User inputtedUser) throws SQLException, DataBaseConnectionException{
        Optional<String> hashedPassword = DataBase.getHashedPassword(inputtedUser.getUsername());
        if (!currentUser.isPresent()) return LoginResult.USER_NOT_FOUND;
        if (hashedPassword.isPresent() && BCrypt.checkpw(inputtedUser.getUsername(), hashedPassword.get())) {
            return LoginResult.SUCCESS;
        } 
        
        return LoginResult.WRONG_PASSWORD;
            
        
    }
}
