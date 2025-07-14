import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Click me!");
        btn.setOnAction(e -> System.out.println("Hello, JavaFX!"));

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("My First JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX app
    }
}
