package phase1;

abstract class Login_Employee extends Login {
    public Login_Employee(String username, String password){
        super(username, password, "Employee");
    }
}
