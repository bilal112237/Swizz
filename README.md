Swizz - Point of Sale (POS) System
Author: M. Bilal Zaki
Project Type: Java Swing Application with MySQL Database

Project Overview
Swizz is a comprehensive Point of Sale (POS) software architecture designed to streamline inventory management, process retail sales, and maintain secure access control. Developed entirely in Java, the application utilizes Java Swing for a responsive graphical user interface and MySQL for robust, relational data persistence.

The system features role-based access control, routing users to either an Admin Dashboard (for inventory and reporting) or a Cashier Dashboard (for transaction processing). The codebase strictly adheres to Object-Oriented Programming (OOP) paradigms and enterprise-level design patterns to ensure scalability, maintainability, and data integrity.

Key Features
1. Admin Module
Inventory Management: Full CRUD operations mapped to the database. Add new products via the AddProductDialog, update pricing, and delete discontinued items.

Sales Reports: View comprehensive transaction histories to analyze sales trends and cashier performance.

Role-Based Security: Secure login system that authenticates credentials against the database and correctly routes administrative users.

2. Cashier Module (ModernPOSDashboard)
User-Friendly Interface: Modern grid layout featuring dynamic buttons for quick product selection.

Dynamic Cart Processing: Add items, view running subtotals, and automatically calculate final bills including taxes or discounts.

Real-Time Stock Control: The system actively prevents the sale of out-of-stock items, validating against the database dynamically.

Atomic Transactions: Updates the database immediately upon checkout, synchronizing the sales, sale_items, and products tables simultaneously.

System Architecture & OOP Principles
The project follows a modular, MVC-inspired directory structure ensuring a clean separation of concerns.

Core OOP Implementations:
Polymorphism & Abstraction: Demonstrated in the model.User abstract class. Both Admin and Cashier inherit from User and provide unique implementations of the polymorphic abstract method getDashboard().

Encapsulation: All entity models (Product, Sale, User, etc.) use strictly private fields with public getter/setter methods (e.g., getId(), getUsername()) to protect data states.

Design Patterns (DAO): Heavy utilization of the Data Access Object (DAO) pattern. Database interactions are abstracted away from the UI into dedicated classes (ProductDAO, SaleDAO, UserDAO, ReportDAO), ensuring the frontend only deals with Java objects, not SQL queries.

Directory Structure:

src/
├── dao/                 # Data Access Objects (SQL queries and DB interactions)
│   ├── ProductDAO.java
│   ├── ReportDAO.java
│   ├── SaleDAO.java
│   └── UserDAO.java
├── db/                  # Database connectivity
│   └── DBConnection.java
├── frontend/            # Java Swing UI Components
│   ├── AddProductDialog.java
│   ├── AdminDashboardUI.java
│   ├── LoginPage.java
│   └── ModernPOSDashboard.java
├── menu/                # Menu configuration and assets
├── model/               # Entity classes (The Data Models)
│   ├── Admin.java
│   ├── Cashier.java
│   ├── Product.java
│   ├── Sale.java
│   ├── SaleItem.java
│   └── User.java        (Abstract Base Class)
├── service/             # Business logic layer
│   ├── ProductService/            
└── Main.java            # Application Entry Point (SwingUtilities.invokeLater)
🗄️ Database Schema (pos_1)
The system relies on a tightly normalized relational database to track all operations:

users: Stores id, username, password, and an ENUM role ('ADMIN', 'CASHIER').

products: Manages inventory with id, name, price, stock_quantity, and category.

sales: Logs the overarching transaction details (sale_date, total_amount, cashier_id).

sale_items: An associative entity linking sales to specific products, tracking unit_price, quantity, and subtotal for line-item accuracy.

🛠️ Technologies Used
Language: Java (JDK)

GUI Framework: Java Swing (javax.swing.*)

Database: MySQL (Using xampp)

IDE: VS Code (Compatible with standard Java build tools)