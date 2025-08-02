import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.mindrot.jbcrypt.BCrypt;

import Exceptions.DataBaseConnectionException;
import Exceptions.UserAlreadyLoggedInException;
import Exceptions.UserNotLoggedInException;

import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;


public class LoginScreen extends JPanel{
    private LoginService loginService = new LoginService();
    public LoginScreen(LoginService loginService){
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
                boolean res = login.attemptLogin(username, password);
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
    boolean handleLogin(
        CurrentLoginState state, 
        String username, 
        String rawPassword
    ) throws SQLException, 
             UserAlreadyLoggedInException, 
             DataBaseConnectionException, 
             UserNotLoggedInException;

    void handleLogout(CurrentLoginState state);
}

class AwaitingCredentialsState implements LoginState {
    @Override
    public boolean handleLogin(
        CurrentLoginState state, 
        String username, 
        String rawPassword
    ) throws SQLException, 
             UserAlreadyLoggedInException, 
             DataBaseConnectionException 
    {
        boolean success = LoginService.checkLoginCredentials(username, rawPassword);
        if (success) {
            state.setState(new LoggedInState());
        }
        return success;
    }

    @Override
    public void handleLogout(CurrentLoginState state) {
        // TODO: throw meaningful exception
    }
}

class LoggedInState implements LoginState {
    @Override
    public boolean handleLogin(
        CurrentLoginState state, 
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
            LoginService.getCurrentUser().getUsername() + 
            " is already logged in."
        );
    }

    @Override
    public void handleLogout(CurrentLoginState state) {
        LoginService.logout();
        state.setState(new AwaitingCredentialsState());
    }
}

class CurrentLoginState {
    private LoginState state;

    public CurrentLoginState() {
        this.state = new AwaitingCredentialsState();
    }

    public void setState(LoginState newState) {
        this.state = newState;
    }

    public boolean attemptLogin(
        String username, 
        String password
    ) throws SQLException, 
             UserAlreadyLoggedInException, 
             DataBaseConnectionException, 
             UserNotLoggedInException
    {
        return state.handleLogin(this, username, password);
    }
}



