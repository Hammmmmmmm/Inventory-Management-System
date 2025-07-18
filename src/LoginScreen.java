import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.mindrot.jbcrypt.BCrypt;

import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;


public class LoginScreen extends JPanel{
    public LoginScreen() throws SQLException, Exception{
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
            Optional<String> hashedPassword = DataBase.getHashedPassword(userField.getText());
            if (hashedPassword.isPresent() && BCrypt.checkpw(userField.getText(), hashedPassword.get())) {
                Main.getInstance().showScreen("dashboard");
            } else {
                int i;
                // TODO Display password or username not correct
            }
            
        });
    }
}
