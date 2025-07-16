import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;


public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    /**
     * Handles the login event after user 
     * presses the login button
     */
    @FXML
    private void handleLogin() {
        String userName = usernameField.getText();
        String passwordHash = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());
        
    }
}
