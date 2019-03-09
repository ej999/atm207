package phase1;

import java.io.*;
import java.util.HashMap;

public class LoginManagerBackup implements Serializable {

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
        //For any exceptions, it will just return the same data as what the current LoginManager has
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


