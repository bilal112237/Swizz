package dao;

import db.DBConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean addProduct(Product product) {
        try{
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO products(name,price,stock_quantity,category) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getStockQuantity());
            ps.setString(4, product.getCategory());
            ps.executeUpdate();
            con.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Product> getAllProducts() throws Exception{
        ArrayList<Product> products = new ArrayList();
        try(Connection con = DBConnection.getConnection();){


            String sql = "SELECT * FROM products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setStock_quantity(rs.getInt("stock_quantity"));
                product.setCategory(rs.getString("category"));

                products.add(product);
            }
            con.close();
            return products;
        }catch (SQLException e){
            e.printStackTrace();
            return products;
        }
        catch (Exception e){
            e.printStackTrace();
            return products;
        }
    }

    // 3. Search Product (For the Search Bar)
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock_quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4. Update an existing product
    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET name = ?, price = ?, stock_quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getStockQuantity());
            stmt.setInt(4, p.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Delete a product
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Tip: This might fail if the product has already been sold (Foreign Key constraint).
            // For a semester project, you can catch this and print "Cannot delete sold item".
            e.printStackTrace();
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
