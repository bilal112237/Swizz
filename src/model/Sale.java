package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Sale {
    ArrayList<SaleItem> items = new ArrayList<>();

    public int getSaleID() {
        return saleID;
    }

    private int saleID;
    private int cashierID;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private double totalAmount;

    private LocalDateTime date;

    public Sale( int cashierID){
        this.cashierID = cashierID;
        this.totalAmount = 0.0;
        this.date = LocalDateTime.now();
    }

    public void addItem(SaleItem item){
        items.add(item);
        this.totalAmount += item.getSubtotal();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ArrayList<SaleItem> getItems(){
        return items;
    }

    public double getTotal() {
        return totalAmount;
    }

    public int getCashierID() {
        return cashierID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }


}
