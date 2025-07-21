import java.sql.SQLException;
import java.util.Optional;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.MultipleUserLoginException;

public final class Authorizer {
    private static Optional<User> currentUser;

    public static Optional<User> getCurrentUser() {return currentUser;}
    public static void login(User user) throws MultipleUserLoginException{
        if (!currentUser.isPresent()) throw new MultipleUserLoginException(
            "Multiple Users attempted to login a the same time");
        if (authorizeLoginCredentials(, null))
    }

    private static boolean authorizeLoginCredentials(User inputtedUser) throws SQLException{
        Optional<String> hashedPassword = DataBase.getHashedPassword(inputtedUser.getUsername());
        if (hashedPassword.isPresent() && BCrypt.checkpw(inputtedUser.getUsername(), hashedPassword.get())) {
            return true;
        }
        return false;
    }
}
