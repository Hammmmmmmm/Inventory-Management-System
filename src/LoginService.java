import java.sql.SQLException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.DataBaseConnectionException;
import Exceptions.UserNotLoggedInException;

public class LoginService {
    private static Optional<User> currentLoggedInUser = Optional.empty();

    
    public User getCurrentLoggedInUser() throws UserNotLoggedInException{
        if (!currentLoggedInUser.isPresent()) throw new UserNotLoggedInException("Tried to get User when no user is logged in");
        return currentLoggedInUser.get();
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

    public static void logout() {
        currentLoggedInUser = Optional.empty();
    }

    
    
}
