package phase1;

import java.io.*;
import java.util.HashMap;

public class LoginManagerBackup implements Serializable {
    //static transient LoginManagerBackup backup;
    HashMap<String, Login> login_map = new HashMap<>();

    LoginManagerBackup() {
        login_map = LoginManager.login_map;
    }

    public LoginManagerBackup returnFileBackup() {
        try {
            FileInputStream file = new FileInputStream("LoginManagerStorage.txt");
            ObjectInputStream object = new ObjectInputStream(file);
            LoginManagerBackup backup = (LoginManagerBackup) object.readObject();
            object.close();
            file.close();
            return backup;
        }
        catch (EOFException f){
            //f.printStackTrace();
            return new LoginManagerBackup();
        }
        catch (IOException i) {
            //i.printStackTrace();
            return new LoginManagerBackup();
        } catch (ClassNotFoundException c) {
            //c.printStackTrace();
            return new LoginManagerBackup();
        }

    }

}


