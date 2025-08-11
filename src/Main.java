import javax.swing.*;

import Exceptions.ScreenRegistrationException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;



interface DatabaseHandler {
    UserRepository getUserRepository();
    UserService getUserService();
    LoginService getLoginService();
}

class ScreenNavigatorWrapper implements ScreenNavigator {
    private final Main main;
    ScreenNavigatorWrapper(Main main) {
        this.main = main;
    }

    @Override 
    public void showScreen(String name) {
        main.showScreen(name);
    }
}

class DatabaseHandlerWrapper implements DatabaseHandler {
    private final Main main;
    DatabaseHandlerWrapper(Main main) {
        this.main = main;
    }

    @Override
    public UserRepository getUserRepository() {
        return main.getUserRepository();
    }
    
    @Override
    public UserService getUserService() {
        return main.getUserService();
    }
    
    @Override
    public LoginService getLoginService() {
        return main.getLoginService();
    }
}

public class Main{
    public static void main(String[] args) throws Exception{
        SwingUtilities.invokeLater(() -> new Main().start());
    }
    private final DatabaseConnection databaseConnection = new SQLiteDataBaseConnection("jdbc:sqlite:lib/other/users.db");
    private final JFrame mainFrame = new JFrame("Inventory Management System");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final Map<String, JPanel> screens = new HashMap<>();
    private final LoginService loginService = new LoginService(new AwaitingCredentialsState());
    private final UserRepository userRepository = new DataBaseUserRepository(databaseConnection);
    private final UserService userService = new DataBaseUserService();


    /**
     * Initial code execute on program start
     * Opens the login window
     */
    private void start(){
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);

        try {
            registerScreen("login", () -> new LoginScreen(loginService));
            registerScreen("dashboard", DashboardScreen::new);
        } catch (Exception ex) {
            throw new ScreenRegistrationException(ex);
        }
        
        showScreen("login");

        mainFrame.add(container);
        mainFrame.setVisible(true);
    }

    private void registerScreen(String name, Supplier<JPanel> panelSupplier) {
        JPanel panel = panelSupplier.get();
        screens.put(name, panel);
        container.add(panel, name);
    }

    // Getters
    void showScreen(String name) {cardLayout.show(container, name);}
    LoginService getLoginService(){return this.loginService;}
    UserRepository getUserRepository() {return this.userRepository;}
    UserService getUserService() {return this.userService;}

    // Screens can call this to request navigation
    public static ScreenNavigator getScreenNavigator() {
        return Main.screenNavigator;
    }

    // Screens or Services can call this to access the database
    public static DatabaseHandler getDatabaseHandler() {
        return Main.databaseHandler;
    }

    private static ScreenNavigator screenNavigator;
    private static DatabaseHandler databaseHandler;

    public Main() {
        Main.screenNavigator = new ScreenNavigatorWrapper(this);
        Main.databaseHandler = new DatabaseHandlerWrapper(this);
    }

    
}
