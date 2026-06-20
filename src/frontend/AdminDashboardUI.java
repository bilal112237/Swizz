package frontend;

import dao.ProductDAO;
import dao.ReportDAO;
import model.Product;
import model.Sale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardUI extends JFrame {
    JTable table;

    private JPanel contentPanel = new JPanel();
    private JTable salesTable = new JTable();
    private ReportDAO reportDAO = new ReportDAO();

    public AdminDashboardUI() {
        setTitle("Swizz | Admin Dashboard");
        setSize(1000, 900); // Slightly taller to ensure buttons are visible
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new GridLayout(6, 1, 0, 10));

        JLabel brand = new JLabel("POS ADMIN", SwingConstants.CENTER);
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnAdd = createSidebarButton("Add Product");
        JButton btnReports = createSidebarButton("Sales Reports");
        JButton btnLogout = createSidebarButton("Logout");

        sidebar.add(brand);
        sidebar.add(btnDashboard);
        sidebar.add(btnAdd);
        sidebar.add(btnReports);
        sidebar.add(btnLogout);

        btnDashboard.addActionListener(e -> showDashboard());
        btnAdd.addActionListener(e -> openAddProductDialog());
        btnReports.addActionListener(e -> showSalesReport());// sale report button functionality
        btnLogout.addActionListener(e -> handleLogout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel adminName = new JLabel("Admin");
        adminName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        topBar.add(title, BorderLayout.WEST);
        topBar.add(adminName, BorderLayout.EAST);

        // ===== CONTENT AREA SETUP =====
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 246, 250));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        // ===== ADD MAIN PANELS TO FRAME =====
        add(sidebar, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Load the dashboard layout (Cards, Table, and Buttons) immediately on boot
        showDashboard();

        setVisible(true);
    }

    // ===== SIDEBAR BUTTON STYLE =====
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(45, 45, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

private void showDashboard() {
        // 1. Clear the panel
        contentPanel.removeAll();

        // 2. Build the Cards (TOP)
        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.add(createStatCard("Total Products", "120"));
        cards.add(createStatCard("Total Sales", "$100,200"));
        cards.add(createStatCard("Users", "8"));

        // 3. Build the Table (CENTER)
        String[] cols = { "ID", "Product Name", "Price", "Stock" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(26);
        JScrollPane tableScroll = new JScrollPane(table);

        // 4. Build the Action Buttons (BOTTOM)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(new Color(245, 246, 250));

        JButton btnEdit = new JButton("Edit Selected");
        btnEdit.setBackground(new Color(33, 150, 243));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        btnEdit.addActionListener(e -> editSelectedProduct());
        btnDelete.addActionListener(e -> deleteSelectedProduct());

        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        // 5. Add all three pieces to the contentPanel
        contentPanel.add(cards, BorderLayout.NORTH);
        contentPanel.add(tableScroll, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH); 

        // 6. Load data and refresh UI
        loadDataIntoTable();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSalesReport() {
        contentPanel.removeAll();

        JLabel title = new JLabel("Sales Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        String[] cols = { "Sale ID", "Date", "Total Amount", "Cashier" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        salesTable = new JTable(model);
        salesTable.setRowHeight(26);

        JScrollPane scrollPane = new JScrollPane(salesTable);

        contentPanel.add(title, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        loadSalesReport(model);

        // 🔥 Double-click to view details
        salesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = salesTable.getSelectedRow();
                    int saleId = Integer.parseInt(
                            salesTable.getValueAt(row, 0).toString());
                    showSaleDetailsDialog(saleId);
                }
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

// ===== UPDATE LOGIC =====
    private void editSelectedProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Extract the current data from the selected row
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        String currentName = table.getValueAt(row, 1).toString();
        String currentPrice = table.getValueAt(row, 2).toString();
        String currentStock = table.getValueAt(row, 3).toString();

        // 2. Create input fields pre-filled with the current data
        JTextField nameField = new JTextField(currentName);
        JTextField priceField = new JTextField(currentPrice);
        JTextField stockField = new JTextField(currentStock);

        Object[] message = {
            "Product Name:", nameField,
            "Price:", priceField,
            "Stock:", stockField
        };

        // 3. Show the dialog box to the user
        int option = JOptionPane.showConfirmDialog(this, message, "Edit Product (ID: " + id + ")", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                // 4. Parse the new values entered by the admin
                String newName = nameField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                int newStock = Integer.parseInt(stockField.getText());

                // 5. Create a Product object with the updated info
                // (If your Product class requires a different constructor, adjust this line)
                Product updatedProduct = new Product();
                updatedProduct.setId(id);
                updatedProduct.setName(newName);
                updatedProduct.setPrice(newPrice);
                updatedProduct.setStockQuantity(newStock);

                // 6. Call the backend DAO
                ProductDAO dao = new ProductDAO();
                boolean success = dao.updateProduct(updatedProduct);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");
                    loadDataIntoTable(); // Refresh the visual table to show the new values
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update product in the database.", "Update Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                // Catch the error if the admin types "abc" into the Price or Stock fields
                JOptionPane.showMessageDialog(this, "Invalid number format! Please ensure Price and Stock are valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// ===== DELETE LOGIC =====
    private void deleteSelectedProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extract ID and Name from the selected row
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        String name = table.getValueAt(row, 1).toString();

        // Confirm deletion with the admin
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to permanently delete '" + name + "'?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            ProductDAO dao = new ProductDAO();
            
            // Call your backend DAO method
            boolean success = dao.deleteProduct(id);

            if (success) {
                // If the database successfully deleted it
                JOptionPane.showMessageDialog(this, "Product deleted successfully.");
                loadDataIntoTable(); // Refresh table to remove deleted item visually
            } else {
                // If it failed (likely due to the Foreign Key constraint from the Sales table)
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete product.\nIt might be linked to existing sales records.", 
                    "Deletion Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openAddProductDialog() {
        AddProductDialog dialog = new AddProductDialog(this);
        dialog.setVisible(true);

        // 🔄 Refresh products table after dialog closes
        showDashboard();
    }

    private void loadSalesReport(DefaultTableModel model) {
        model.setRowCount(0);

        List<String[]> report = reportDAO.getSalesReport();

        for (String[] row : report) {
            model.addRow(new Object[] {
                    row[0], // Sale ID
                    row[1], // Date
                    row[2], // Total
                    row[3] // Cashier
            });
        }
    }

    private void showSaleDetailsDialog(int saleId) {
        JDialog dialog = new JDialog(this, "Sale Details - ID " + saleId, true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        List<String> items = reportDAO.getSaleDetails(saleId);

        if (items.isEmpty()) {
            textArea.setText("No details found for this sale.");
        } else {
            for (String line : items) {
                textArea.append(line + "\n");
            }
        }

        dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());

        dialog.add(closeBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ===== STAT CARD =====
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(new Color(33, 150, 243));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    public void handleLogout() {
        dispose();
        new LoginPage();
    }

    // Inside your class
    public void loadDataIntoTable() {
        ProductDAO dao = new ProductDAO();
        // 1. Fetch data from backend
        ArrayList<Product> productList = null;
        try {
            productList = dao.getAllProducts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 2. Get the table model
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing data

        // 3. Loop through list and add rows
        for (Product p : productList) {
            Object[] row = {
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    p.getCategory()
            };
            model.addRow(row);
        }
    }
}
