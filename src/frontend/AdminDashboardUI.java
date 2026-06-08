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
    JTable table ;

    private JPanel contentPanel = new JPanel();
    private JTable salesTable = new JTable();
    private ReportDAO reportDAO = new ReportDAO();

    public AdminDashboardUI() {
        setTitle("Swizz | Admin Dashboard");
        setSize(1000, 600);
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
        btnReports.addActionListener(e -> showSalesReport());//sale report button functionality
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

        // ===== CONTENT AREA =====
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 246, 250));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ----- CARDS -----
        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.add(createStatCard("Total Products", "120"));
        cards.add(createStatCard("Total Sales", "$45,200"));
        cards.add(createStatCard("Users", "8"));

        // ----- TABLE -----
        String[] cols = {"ID", "Product Name", "Price", "Stock"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(26);

        JScrollPane tableScroll = new JScrollPane(table);

        contentPanel.add(cards, BorderLayout.NORTH);
        contentPanel.add(tableScroll, BorderLayout.CENTER);

        // ===== ADD TO FRAME =====
        add(sidebar, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        loadDataIntoTable();

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
        contentPanel.removeAll();

        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.add(createStatCard("Total Products", "120"));
        cards.add(createStatCard("Total Sales", "$45,200"));
        cards.add(createStatCard("Users", "8"));

        String[] cols = {"ID", "Product Name", "Price", "Stock"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(26);

        JScrollPane tableScroll = new JScrollPane(table);

        contentPanel.add(cards, BorderLayout.NORTH);
        contentPanel.add(tableScroll, BorderLayout.CENTER);

        loadDataIntoTable();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSalesReport() {
        contentPanel.removeAll();

        JLabel title = new JLabel("Sales Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        String[] cols = {"Sale ID", "Date", "Total Amount", "Cashier"};
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
                            salesTable.getValueAt(row, 0).toString()
                    );
                    showSaleDetailsDialog(saleId);
                }
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
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
            model.addRow(new Object[]{
                    row[0], // Sale ID
                    row[1], // Date
                    row[2], // Total
                    row[3]  // Cashier
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

    public void handleLogout(){
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
