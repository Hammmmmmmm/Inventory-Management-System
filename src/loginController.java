import javafx.beans.property.ObjectPropertyBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Optional;
import javafx.event.ActionEvent;




public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    /**
     * Handles the login event after user 
     * presses the login button
     */
    @FXML
    private void handleLogin(ActionEvent event) throws SQLException, Exception{
        String userName = usernameField.getText();
        String passwordHashInput = BCrypt.hashpw(passwordField.getText(), 
                              BCrypt.gensalt());

        // The Stored password hash for inputted userName
        Optional<String> passWordHashDataBase = DataBase.getHashedPassword(userName);
        if (!passWordHashDataBase.isPresent()) System.out.println("Password not found"); // TODO Temporary
        if (BCrypt.checkpw(passwordHashInput, passWordHashDataBase.get())) changeWindow(Main.WINDOW.DASHBOARD, event);
    }

    private void changeWindow(Main.WINDOW window, ActionEvent event) throws Exception{
        Parent windowRoot = FXMLLoader.load(getClass().getResource(window.getXMLPath()));
        Scene windowScene = new Scene(windowRoot);

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene
        stage.setScene(windowScene);
        stage.setTitle("Inventory-Management-System");
        stage.show();
    }
}
