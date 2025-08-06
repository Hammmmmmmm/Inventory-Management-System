import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends JPanel {

    public DashboardScreen() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to the Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton logoutButton = new JButton("Log Out");

        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);


        logoutButton.addActionListener(e ->
            Main.getScreenNavigator().showScreen("login")
        );
    }
}