package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/pos_1";
    private static final String username = "root";
    private static final String password = "";

    public static Connection getConnection() throws Exception{
       return DriverManager.getConnection(url,username,password);
    }
}
