package phase1;

abstract class Login_Employee implements Login {
    private String username;
    private String password;

    Login_Employee(){

    }
    public Login_Employee(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
}
