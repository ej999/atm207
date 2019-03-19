package phase2;

import java.io.*;
import java.util.HashMap;

class LoginManagerBackup implements Serializable {

    HashMap<String, SystemUser> login_map;
    int deleted = 0;

    LoginManagerBackup() {
        this.login_map = LoginManager.login_map;
    }

    //This is for when we delete
    LoginManagerBackup(String deleter) {
        this.deleted = 1;

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

    public void deleteBackup() {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("phase1/LoginManagerStorage.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new LoginManagerBackup("deleted"));
            out.close();
            fileOut.close();
            System.err.print("Backup deleted.");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    HashMap<String, SystemUser> loadCustom(String filename) {
        try {
            FileInputStream file = new FileInputStream("phase1/" + filename + ".txt");
            ObjectInputStream object = new ObjectInputStream(file);
            LoginManagerBackup backup = (LoginManagerBackup) object.readObject();
            object.close();
            file.close();
            return backup.login_map;
        } catch (IOException | ClassNotFoundException f) {
            //f.printStackTrace();
            return LoginManager.login_map;
        }

    }

}


