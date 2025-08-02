import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.*;



public class LoginScreen extends JPanel{
    private LoginService loginService;
    public LoginScreen(LoginService loginService){
        this.loginService = loginService;
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
                boolean res = this.loginService.attemptLogin(username, password);
                if (res) {
                    ScreenNavigator navigator = Main.getScreenNavigator();
                    navigator.showScreen("dashboard");
                } else {
                    JOptionPane.showMessageDialog(null, "Username or Password was incorrect");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Login failed: " + ex.getMessage());
            }
        });
    }
}






