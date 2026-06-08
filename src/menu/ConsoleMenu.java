package menu;

import dao.ProductDAO;
import dao.SaleDAO;
import dao.UserDAO;
import dao.ReportDAO; // You added this in the file tree
import model.Product;
import model.Sale;
import model.SaleItem;
import model.User;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    // 1. Connect the DAOs (The Backend)
    private final ProductDAO productDAO = new ProductDAO();
    private final SaleDAO saleDAO = new SaleDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ReportDAO reportDAO = new ReportDAO();

    private final Scanner sc = new Scanner(System.in);
    private User currentUser; // Stores who is currently logged in

    public void start() {
        System.out.println("Welcome to POS System");

        // 2. The Login Loop
        while (currentUser == null) {
            System.out.print("\nUsername: ");
            String username = sc.next();
            System.out.print("Password: ");
            String password = sc.next();

            currentUser = userDAO.login(username, password);

            if (currentUser != null) {
                System.out.println("Login Successful! Welcome " + currentUser.getUsername());
                showRoleBasedMenu();
            } else {
                System.out.println("Invalid Credentials. Try again.");
            }
        }
    }

    // 3. Router: Sends user to the correct screen based on Role
    private void showRoleBasedMenu() {
        String role = currentUser.getRole(); // Assuming User model has getRole()

        if ("ADMIN".equalsIgnoreCase(role)) {
            adminDashboard();
        } else {
            cashierDashboard();
        }
    }

    // ================= ADMIN DASHBOARD =================
    private void adminDashboard() {
        while (true) {
            System.out.println("\n==== ADMIN DASHBOARD ====");
            System.out.println("1. Add New Product");
            System.out.println("2. View All Products");
            System.out.println("3. Delete Product");
            System.out.println("4. View Sales Reports");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> addProductUI();
                case 2 -> viewProductsUI();
                case 3 -> deleteProductUI();
                case 4 -> viewReportsUI(); // New Feature
                case 5 -> {
                    System.out.println("Logging out...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    // ================= CASHIER DASHBOARD =================
    private void cashierDashboard() {
        while (true) {
            System.out.println("\n==== CASHIER DASHBOARD ====");
            System.out.println("1. New Sale (Checkout)");
            System.out.println("2. View Products (Price Check)");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> processSaleUI();
                case 2 -> viewProductsUI();
                case 3 -> {
                    System.out.println("Logging out...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    // ================= FEATURES =================

    private void addProductUI() {
        System.out.println("\n--- Add Product ---");
        sc.nextLine(); // Fix scanner bug

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Price: ");
        double price = sc.nextDouble();

        System.out.print("Stock Quantity: ");
        int stock = sc.nextInt();

        System.out.println("Product Category:");
        String category = sc.nextLine();
        sc.nextLine();

        // Pass 0 as ID because DB auto-increments it
        Product newProd = new Product( name, price, stock,category);

        if (productDAO.addProduct(newProd)) {
            System.out.println("Product Added Successfully!");
        } else {
            System.out.println("Failed to add product.");
        }
    }

    private void viewProductsUI() {
        System.out.println("\n--- Product List ---");
        try {
            List<Product> list = productDAO.getAllProducts();
            System.out.printf("%-5s %-20s %-10s %-5s\n", "ID", "Name", "Price", "Stock");
            System.out.println("---------------------------------------------");

        for (Product p : list) {
            System.out.printf("%-5d %-20s $%-9.2f %-5d\n",
                    p.getId(), p.getName(), p.getPrice(), p.getStockQuantity());
        }
    }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void deleteProductUI() {
        System.out.print("Enter Product ID to delete: ");
        int id = sc.nextInt();
        if (productDAO.deleteProduct(id)) { // You need to ensure deleteProduct exists in ProductDAO
            System.out.println("Product deleted.");
        } else {
            System.out.println("Error deleting product.");
        }
    }

// Inside ConsoleMenu.java

    private void viewReportsUI() {
        System.out.println("\n--- Sales History ---");

        // 1. Show the Summary List first
        List<String[]> report = reportDAO.getSalesReport();
        if (report.isEmpty()) {
            System.out.println("No sales records found.");
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-10s\n", "ID", "Date", "Total", "Cashier");
        System.out.println("-------------------------------------------------------");
        for(String[] row : report) {
            // row[0]=ID, row[1]=Date, row[2]=Total, row[3]=Cashier
            System.out.printf("%-5s %-20s $%-9s %-10s\n", row[0], row[1], row[2], row[3]);
        }

        // 2. Ask user if they want details
        System.out.println("\nOptions:");
        System.out.println("Enter Sale ID to view items (or 0 to go back): ");

        int choice = sc.nextInt();
        if (choice != 0) {
            showSaleDetails(choice);
        }
    }

    // Helper method to display the detailed receipt
    private void showSaleDetails(int saleId) {
        List<String> items = reportDAO.getSaleDetails(saleId);

        if (items.isEmpty()) {
            System.out.println("Sale ID not found or has no items.");
        } else {
            System.out.println("\n--- Details for Sale #" + saleId + " ---");
            for (String itemLine : items) {
                System.out.println(itemLine);
            }
            System.out.println("----------------------------------");
            System.out.println("Press Enter to continue...");
            try { System.in.read(); } catch (Exception e) {} // Pause
        }
    }

    // === THE MAIN EVENT: MAKING A SALE ===
    private void processSaleUI() {
        System.out.println("\n--- New Sale Transaction ---");
        Sale sale = new Sale(currentUser.getId()); // Create a sale for the current cashier
        boolean selling = true;

        while (selling) {
            System.out.print("Enter Product ID (or 0 to Finish): ");
            int prodId = sc.nextInt();

            if (prodId == 0) {
                break;
            }

            Product p = productDAO.getProductById(prodId);
            if (p != null) {
                System.out.print("Enter Quantity: ");
                int qty = sc.nextInt();

                if (qty <= p.getStockQuantity()) {
                    // Create SaleItem and add to Cart
                    SaleItem item = new SaleItem(p.getId(),sale.getSaleID(), p.getPrice(), qty);
                    sale.addItem(item);
                    System.out.println("Added: " + p.getName() + " x" + qty + " (Subtotal: " + item.getSubtotal() + ")");
                } else {
                    System.out.println("Error: Insufficient Stock! Available: " + p.getStockQuantity());
                }
            } else {
                System.out.println("Product not found!");
            }
        }

        if (sale.getItems().isEmpty()) {
            System.out.println("Sale Cancelled (Empty Cart).");
            return;
        }

        // Final Checkout
        System.out.println("Total Amount: $" + sale.getTotalAmount());
        System.out.print("Confirm Sale? (y/n): ");
        String confirm = sc.next();

        if (confirm.equalsIgnoreCase("y")) {
            if (saleDAO.processSale(sale)) {
                System.out.println("Sale Completed Successfully!");
                // Here you could call ReceiptBuilder.generateReceipt(sale) if you want
            } else {
                System.out.println("Sale Failed (Database Error).");
            }
        } else {
            System.out.println("Sale Cancelled.");
        }
    }
}