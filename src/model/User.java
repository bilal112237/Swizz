package model;

public abstract class User {
    private int id;
    private String username;
    private String role;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public User(){}

    public User(int id,String username, String role){
        this.id = id;
        this.username = username;
        this.role = role;
    }

    //polymorphism abstract Method
    public abstract String getDashboard();
}
