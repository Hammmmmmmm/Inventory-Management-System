import javax.swing.*;

import Exceptions.ScreenRegistrationException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

interface ScreenNavigator {
    void showScreen(String name);
}

public class Main implements ScreenNavigator{
    public static void main(String[] args) throws Exception{
        //TODO create user table
        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private final JFrame mainFrame = new JFrame("Inventory Management System");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final Map<String, JPanel> screens = new HashMap<>();
    private final LoginService loginService = new LoginService(new AwaitingCredentialsState());

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

    public void showScreen(String name) {
        cardLayout.show(container, name);
    }

    public LoginService getLoginService(){
        return this.loginService;
    }

    // Screens can call this to request navigation
    public static ScreenNavigator getScreenNavigator() {
        return Main.screenNavigator;
    }

    private static ScreenNavigator screenNavigator;

    public Main() {
        Main.screenNavigator = this;
    }

    
}
