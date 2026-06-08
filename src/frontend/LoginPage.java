package frontend;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class LoginPage extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginPage() {
        setTitle("Swizz");
        setSize(520, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(25, 118, 210)); // professional blue
        leftPanel.setPreferredSize(new Dimension(180, 0));
        leftPanel.setLayout(new GridBagLayout());

        JLabel logo = new JLabel("Swizz");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        logo.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Point of Sale");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.WHITE);

        JPanel logoBox = new JPanel(new GridLayout(2, 1));
        logoBox.setOpaque(false);
        logoBox.add(logo);
        logoBox.add(subtitle);

        leftPanel.add(logoBox);

        // ===== RIGHT LOGIN PANEL =====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(33, 33, 33));

        userField = new JTextField();
        userField.setPreferredSize(new Dimension(220, 38));
        userField.setBorder(BorderFactory.createTitledBorder("Username"));

        passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(220, 38));
        passField.setBorder(BorderFactory.createTitledBorder("Password"));


        JButton loginButton = new JButton("LOGIN");
        loginButton.setPreferredSize(new Dimension(220, 40));
        loginButton.setBackground(new Color(25, 118, 210));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                // 1. Call your existing Backend logic
                // Assuming you have a method like authenticate() in Crud or UserDAO
                User user = UserDAO.login(username, password);

                if (user != null) {
                    // 2. Success: Check Role and Redirect
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    dispose(); // Close login window

                    if (user.getRole().equalsIgnoreCase("Admin")) {
                        new AdminDashboardUI().setVisible(true);
                    } else {
                        new ModernPOSDashboard(user).setVisible(true);
                    }
                } else {
                    // 3. Failure
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            });

        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(title, gbc);

        gbc.gridy++;
        rightPanel.add(userField, gbc);

        gbc.gridy++;
        rightPanel.add(passField, gbc);

        gbc.gridy++;
        rightPanel.add(loginButton, gbc);

        // ===== ADD PANELS =====
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

}
