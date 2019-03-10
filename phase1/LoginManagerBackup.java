package phase1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;

class LoginManagerBackup implements Serializable {

    final HashMap<String, Login> login_map;

    LoginManagerBackup() {
        login_map = LoginManager.login_map;
    }

    LoginManagerBackup returnFileBackup() {
        try {
            FileInputStream file = new FileInputStream("phase1/LoginManagerStorage.txt");
            ObjectInputStream object = new ObjectInputStream(file);
            LoginManagerBackup backup = (LoginManagerBackup) object.readObject();
            object.close();
            file.close();
            return backup;
        }
        //For any exceptions, it will just return the same data as what the current LoginManager has
        catch (IOException | ClassNotFoundException f) {
            //f.printStackTrace();
            return new LoginManagerBackup();
        }//i.printStackTrace();
        //c.printStackTrace();


    }

}


