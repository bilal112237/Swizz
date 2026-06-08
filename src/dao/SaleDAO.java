package dao;

import db.DBConnection;
import model.Sale;
import model.SaleItem;

import java.sql.*;

public class SaleDAO {

    public boolean processSale(Sale sale) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement saleStm = null;
        PreparedStatement itemStm = null;
        PreparedStatement stockStm = null;

        String saleSql = "INSERT INTO sales(total_amount,cashier_id) VALUES(?,?)";
        String itemSql = "INSERT INTO sale_items(product_id,sale_id,unit_price,quantity,subtotal) VALUES(?,?,?,?,?)";
        String stockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";

        try {
            con = DBConnection.getConnection();

            //Auto Commit Closed
            con.setAutoCommit(false);

            //Preparing Sale Statement
            saleStm = con.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS);
            saleStm.setDouble(1, sale.getTotal());
            saleStm.setInt(2, sale.getCashierID());
            saleStm.executeUpdate();

            rs = saleStm.getGeneratedKeys();
            int saleId = 0;
            if (rs.next()) {
                saleId = rs.getInt(1);
                sale.setSaleID(saleId);
            }

            itemStm = con.prepareStatement(itemSql);
            stockStm = con.prepareStatement(stockSql);

            for (SaleItem item : sale.getItems()) {
                itemStm.setInt(1, item.getProductID());
                itemStm.setInt(2, saleId);
                itemStm.setDouble(3, item.getUnitPrice());
                itemStm.setInt(4, item.getQuantity());
                itemStm.setDouble(5, item.getSubtotal());
                itemStm.executeUpdate();

                stockStm.setInt(1, item.getQuantity());
                stockStm.setInt(2, item.getProductID());
                stockStm.executeUpdate();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try{if (con!=null) con.rollback();}catch(SQLException e){e.printStackTrace();}

            try {if (con != null) con.setAutoCommit(true);} catch (SQLException e) {e.printStackTrace();}

            try { if (rs != null) rs.close(); } catch (SQLException e) {e.printStackTrace();}

            try { if (saleStm != null) saleStm.close(); } catch (SQLException e) {e.printStackTrace();}

            try { if (itemStm != null) itemStm.close(); } catch (SQLException e) {e.printStackTrace();}

            try { if (stockStm != null) stockStm.close(); } catch (SQLException e) {e.printStackTrace();}

            try { if (con != null) con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }
}
