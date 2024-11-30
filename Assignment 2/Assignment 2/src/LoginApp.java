import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginApp extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/softwaretesting?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678";

    public LoginApp() {
        setTitle("Login Screen");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        panel.add(loginButton);

        add(panel);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both email and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String userName = authenticateUser(email, password);
            if (userName != null) {
                JOptionPane.showMessageDialog(null, "Welcome, " + userName + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String authenticateUser(String email, String password) {
        String userName = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Name FROM User WHERE Email = ? AND Password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.setString(2, password); // Assuming passwords are stored in plaintext (not recommended for production)
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        userName = rs.getString("Name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return userName;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginApp loginApp = new LoginApp();
            loginApp.setVisible(true);
        });
    }
}
