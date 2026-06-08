package model;

public class SaleItem {

    private int productID;

    public int getSaleID() {
        return saleID;
    }

    private int saleID;

    public double getUnitPrice() {
        return unitPrice;
    }

    private double unitPrice;
    private int quantity;
    private double subtotal;

    public SaleItem(int id, double unitPrice, int quantity){
        this.productID = id;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice * quantity;
    }

    public SaleItem(int id,int saleId, double unitPrice, int quantity){
        this.productID = id;
        this.saleID = saleId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice * quantity;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
