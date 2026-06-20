<p align="center">
  <img src="link-to-your-banner-image.png" alt="Swizz POS Banner" width="100%">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge" />
</p>

<h1 align="center">Swizz - Point of Sale (POS) System</h1>


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
