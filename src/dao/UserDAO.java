package dao;

import db.DBConnection;
import model.Admin;
import model.Cashier;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    static String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

    public static User login(String username, String password){
        try (Connection con = DBConnection.getConnection();
        PreparedStatement stmnt = con.prepareStatement(sql)){
            stmnt.setString(1, username);
            stmnt.setString(2,password);

            ResultSet rs = stmnt.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String role = rs.getString("role");
                if ("ADMIN".equals(role)){
                    return new Admin(id, username);
                }
                else if ("CASHIER".equals(role)){
                    return new Cashier(id, username);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
