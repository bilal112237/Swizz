package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    // Returns a list of strings like "Sale #1 - $50.00 - by Admin - [Date]"
    // In a real app, you'd return Objects, but Strings are easier for the GUI table right now.
    public List<String[]> getSalesReport() {
        List<String[]> reportData = new ArrayList<>();
        String sql = "SELECT s.id, s.sale_date, s.total_amount, u.username " +
                "FROM sales s " +
                "JOIN users u ON s.cashier_id = u.id " +
                "ORDER BY s.sale_date DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String date = rs.getTimestamp("sale_date").toString();
                String total = String.valueOf(rs.getDouble("total_amount"));
                String cashier = rs.getString("username");

                reportData.add(new String[]{id, date, total, cashier});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return reportData;
    }

    // Inside ReportDAO.java

    public List<String> getSaleDetails(int saleId) {
        List<String> details = new ArrayList<>();

        // We join sales_items with products to get the NAME of the item sold
        String sql = "SELECT p.name, si.quantity, si.subtotal " +
                "FROM sale_items si " +
                "JOIN products p ON si.product_id = p.id " +
                "WHERE si.sale_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String prodName = rs.getString("name");
                int qty = rs.getInt("quantity");
                double subtotal = rs.getDouble("subtotal");

                details.add(qty + "x " + prodName + " ($" + subtotal + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return details;
    }
}