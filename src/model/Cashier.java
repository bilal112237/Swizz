package model;

public class Cashier extends User{
    public Cashier(int id,String username){
        super(id,username, "Cashier");
    }

    @Override
    public String getDashboard() {
        return "Limited Access To Dashboard";
    }
}
