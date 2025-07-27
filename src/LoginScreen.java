import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.DataBaseConnectionException;
import Exceptions.UserAlreadyLoggedInException;

import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;


public class LoginScreen extends JPanel{
    private final CurrentLoginState state;
    private static Optional<User> currentUser = Optional.empty();

    public LoginScreen() {
        this(new RealLoginService()); 
    }

    static Optional<User> getCurrentUser() {return LoginScreen.currentUser;}
    public LoginScreen(LoginService loginService){
        this.state = new CurrentLoginState(loginService);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 2));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        form.add(new JLabel("Username:"));
        form.add(userField);
        form.add(new JLabel("Password:"));
        form.add(passField);
        form.add(new JLabel());
        form.add(loginButton);

        add(form, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            try {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                boolean res = state.attemptLogin(username, password);
                if (res) {
                    // TODO: switch to dashboard
                } else {
                    // TODO: show error
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Login failed: " + ex.getMessage());
            }
        });
    }
}

interface LoginState {
    boolean handleLogin(CurrentLoginState state, String username, String password) throws SQLException, UserAlreadyLoggedInException, DataBaseConnectionException;
}

interface LoginService {
    public boolean checkLoginCredentials(String username, String password) throws SQLException, UserAlreadyLoggedInException, DataBaseConnectionException;
}

class RealLoginService implements LoginService {
    public boolean checkLoginCredentials(String username, String password) throws SQLException, UserAlreadyLoggedInException, DataBaseConnectionException{
        return DataBase.checkLoginCredentials(username, password);
    }

}

class CurrentLoginState {
    private LoginState state = new AwaitingCredentialsState();
    private final LoginService loginService;

    CurrentLoginState(LoginService loginService) {
        this.loginService = loginService;
    }

    public void setState(LoginState state) {this.state = state;}
    public boolean attemptLogin(String username, String password) {
        try {
            return state.handleLogin(this, username, password);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Login failed: " + ex.getMessage());
            return false;
        }
        
    }
    public LoginService getLoginService() {
        return this.loginService;
    }
}


class AwaitingCredentialsState implements LoginState {
    public boolean handleLogin(CurrentLoginState cls, String username, String password) throws SQLException, UserAlreadyLoggedInException, DataBaseConnectionException{
       boolean res = cls.getLoginService().checkLoginCredentials(username, password);
       if (res) cls.setState(new LoggedInState());
       return res;
    }
}

class LoggedInState implements LoginState {
    public boolean handleLogin(CurrentLoginState cls, String username, String password) throws SQLException, UserAlreadyLoggedInException, DataBaseConnectionException{
        JOptionPane.showMessageDialog(null, "Cannot login, your already logged in", 
            "OK", JOptionPane.INFORMATION_MESSAGE);
        throw new UserAlreadyLoggedInException(
            "User: " + username + "tried to login when User: " + 
            LoginScreen.getCurrentUser().get().getUsername());
    }
}
