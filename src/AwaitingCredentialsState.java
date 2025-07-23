import java.sql.SQLException;

import Exceptions.DataBaseConnectionException;

public class AwaitingCredentialsState implements LoginState {
    public void handleLogin(CurrentLoginState currentLoginState, String username, String password) {
        if ()
    }

    /**
     * Handles Login logic
     * @param user User object, only uses username and passwordHash
     * @throws MultipleUserLoginException
     */
    private static boolean tryLogin(String username, String password) throws SQLException, DataBaseConnectionException{
        boolean result = DataBase.checkLoginCredentials(user);
        if (result) login(user);

        return result;
    }

}