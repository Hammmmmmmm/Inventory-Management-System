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
    private final CurrentLoginState state = new CurrentLoginState();
    private static Optional<User> currentUser = Optional.empty();

    static Optional<User> getCurrentUser() {return LoginScreen.currentUser;}
    public LoginScreen(){
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
                    boolean res = state.attemptLogin(userField.getText(), passField.getPassword().toString());
                    if (res) // TODO changes screen to the dashboard 
                    else // TODO notify user login failed
                    
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            
            
        });
    }
}

interface LoginState {
    boolean handleLogin(CurrentLoginState state, String username, String password);
}


class CurrentLoginState {
    private LoginState state = new AwaitingCredentialsState();
    public void setState(LoginState state) {this.state = state;}
    public boolean attemptLogin(String username, String password) {
        return state.handleLogin(this, username, password);
    }
}


class AwaitingCredentialsState implements LoginState {
    public boolean handleLogin(CurrentLoginState cls, String username, String password) {
       boolean res = tryLogin(username, password); 
       if (res) cls.setState(new LoggedInState());
       return res;
    }

    /**
     * Handles Login logic
     * @param user User object, only uses username and passwordHash
     * @throws MultipleUserLoginException
     */
    private static boolean tryLogin(String username, String password) throws SQLException, DataBaseConnectionException{
        return DataBase.checkLoginCredentials(username, password);
    }

}

class LoggedInState implements LoginState {
    public boolean handleLogin(CurrentLoginState cls, String username, String password) {
        JOptionPane.showMessageDialog(null, "Cannot login, your already logged in", 
            "OK", JOptionPane.INFORMATION_MESSAGE);
        throw new UserAlreadyLoggedInException(
            "User: " + username + "tried to login when User: " + 
            LoginScreen.getCurrentUser().get().getUsername());
    }
}
