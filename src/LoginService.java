import java.sql.SQLException;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.DataBaseConnectionException;
import Exceptions.UserAlreadyLoggedInException;
import Exceptions.UserNotLoggedInException;

public class LoginService {
    private Optional<User> currentLoggedInUser = Optional.empty();
    private LoginState loginState;

    LoginService(LoginState loginState) {
        this.loginState = loginState;
    }

    public void setState(LoginState newState) {
        this.loginState = newState;
    }

    public boolean attemptLogin(
        String username, 
        String password
    ) throws SQLException, 
             UserAlreadyLoggedInException, 
             DataBaseConnectionException, 
             UserNotLoggedInException
    {
        return this.loginState.handleLogin(this, username, password);
    }

    public User getCurrentUser() throws UserNotLoggedInException{
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
    public boolean checkLoginCredentials(String username, String password) throws SQLException, DataBaseConnectionException{
        Optional<String> hashedPassword = DataBase.getHashedPassword(username);
        if (hashedPassword.isPresent() && 
            BCrypt.checkpw(username, hashedPassword.get())) {
                return true; // Credentials correct
        } 
        
        return false;
    }
    
    public void logout() {
        currentLoggedInUser = Optional.empty();
    }
}

interface LoginState {
    boolean handleLogin(
        LoginService loginService, 
        String username, 
        String rawPassword
    ) throws SQLException, 
             UserAlreadyLoggedInException, 
             DataBaseConnectionException, 
             UserNotLoggedInException;

    void handleLogout(LoginService loginService);
}

class AwaitingCredentialsState implements LoginState {
    @Override
    public boolean handleLogin(
        LoginService loginService, 
        String username, 
        String rawPassword
    ) throws SQLException, 
             UserAlreadyLoggedInException, 
             DataBaseConnectionException 
    {
        boolean success = loginService.checkLoginCredentials(username, rawPassword);
        if (success) {
            loginService.setState(new LoggedInState());
        }
        return success;
    }

    @Override
    public void handleLogout(LoginService state) {
        // TODO: throw meaningful exception
    }
}

class LoggedInState implements LoginState {
    @Override
    public boolean handleLogin(
        LoginService loginService, 
        String username, 
        String password
    ) throws SQLException, 
             UserAlreadyLoggedInException, 
             DataBaseConnectionException, 
             UserNotLoggedInException 
    {
        JOptionPane.showMessageDialog(
            null, 
            "Cannot login, you're already logged in.", 
            "OK", 
            JOptionPane.INFORMATION_MESSAGE
        );

        throw new UserAlreadyLoggedInException(
            "User: " + username + 
            " tried to login while User: " + 
            state.getCurrentUser().getUsername() + 
            " is already logged in."
        );
    }

    @Override
    public void handleLogout(LoginService loginService) {
        loginService.logout();
        loginService.setState(new AwaitingCredentialsState());
    }
}
