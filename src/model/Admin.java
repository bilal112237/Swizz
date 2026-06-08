package model;

public class Admin extends User{
    public Admin(int id,String username){
        super(id,username, "Admin");
    }

    @Override
    public String getDashboard() {
        return "Full Access To Dashboard";
    }
}
