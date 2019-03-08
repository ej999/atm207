package phase1;

import java.util.HashMap;

public class LoginManagerBackup {
    static HashMap<String, Login> login_map = new HashMap<>();
    LoginManagerBackup(){
        login_map = LoginManager.login_map;
    }
}
