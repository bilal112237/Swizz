package service;

import dao.ProductDAO;
import model.Product;

import java.util.List;

public class ProductService {
    ProductDAO dao = new ProductDAO();

    public void addNewProduct(String name,double price,int stock_quantity,String category){
        Product p = new Product(name, price,stock_quantity,category);
        try{
            dao.addProduct(p);
            System.out.println("Product Added To db!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProducts(){
        try{
            List<Product> products = dao.getAllProducts();

            for (Product product: products){
                System.out.printf("ID: %9d Name: %15s \n",product.getId(),product.getName() );
                System.out.printf("Price: %5.2f Stock: %5d \n" ,product.getPrice(),product.getStockQuantity());
                System.out.println("Category: " + product.getCategory());
                System.out.println("\n\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
