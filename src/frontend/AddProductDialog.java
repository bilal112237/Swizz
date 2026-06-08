package frontend;

import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import java.awt.*;

public class AddProductDialog extends JDialog {

    public AddProductDialog(JFrame parent) {
        super(parent, "Add New Product", true);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 118, 210));
        header.setPreferredSize(new Dimension(0, 70));

        JLabel title = new JLabel("Add Product");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        header.add(title, BorderLayout.WEST);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Product Name"));

        JTextField priceField = new JTextField();
        priceField.setBorder(BorderFactory.createTitledBorder("Price"));

        JTextField stockField = new JTextField();
        stockField.setBorder(BorderFactory.createTitledBorder("Stock Quantity"));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(25, 133, 63));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setFocusPainted(false);

        saveBtn.addActionListener(e -> {
            try {

                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText()); // handling number format errors!
                int stock = Integer.parseInt(stockField.getText());

                Product newProduct = new Product( name, price, stock, "General");
                ProductDAO dao = new ProductDAO();

                // 3. Call Backend
                boolean success = dao.addProduct(newProduct);

                // 4. Update UI
                if (success) {
                    JOptionPane.showMessageDialog(this, "Product Added!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add product.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for price/stock.");
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setFocusPainted(false);

        buttonsPanel.add(saveBtn);
        buttonsPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(nameField, gbc);
        gbc.gridy++; formPanel.add(priceField, gbc);
        gbc.gridy++; formPanel.add(stockField, gbc);
        gbc.gridy++; formPanel.add(buttonsPanel, gbc);

        add(header, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Product Saved!")
        );

        setVisible(true);
    }
}

