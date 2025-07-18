import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
public class Main{
    public static void main(String[] args) {
        DataBase.createUsersTable();
        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private final JFrame mainFrame = new JFrame("Inventory Management System");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final Map<String, JPanel> screens = new HashMap<>();

    /**
     * Initial code execute on program start
     * Opens the login window
     */
    private void start() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);

        registerScreen("login", LoginScreen::new);
        registerScreen("dashboard", DashboardScreen::new);
        registerScreen("settings", SettingsScreen::new);

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

    // Screens can call this to request navigation
    public static Main getInstance() {
        return instance;
    }

    private static Main instance;

    public Main() {
        instance = this;
    }

    
}
