package phase1;

public abstract class Employees {
    String username;
    String password;

    public Employees(){

    }
    public Employees(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }
}
