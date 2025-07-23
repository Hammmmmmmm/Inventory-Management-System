import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.mindrot.jbcrypt.BCrypt;
import java.awt.*;
import java.util.Optional;


public class LoginScreen extends JPanel{
    private final CurrentLoginState state = new CurrentLoginState();

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
                    state.attemptLogin(userField.getText(), passField.getPassword().toString());

                    
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            
            
        });
    }
}
