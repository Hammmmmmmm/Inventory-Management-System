import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;



public class Main extends Application{
    public enum WINDOW{
        LOGIN,
        DASHBOARD;
        public String getXMLPath() {
            return switch(this) {
                case LOGIN -> "login.fxml";
                case DASHBOARD -> "dashBoard.fxml";
            };
        }

        
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Initializes databaseTable if not already 
        DataBase.createUsersTable();

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml")); 
        Scene scene = new Scene(root, 500, 300);

        primaryStage.setTitle("Inventory-Management-System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Start program
    }
}
