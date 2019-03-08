package phase1;

abstract class Login_Employee extends Login {
    Login_Employee(String username, String password){
        this(username, password, "Employee");
    }

    /** 3 parameter constructor: different kind of login type required for different employees. */
    Login_Employee(String username, String password, String loginType){
        super(username, password, loginType);
    }
}
