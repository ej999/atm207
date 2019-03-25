package ATM;

import java.util.HashMap;

/**
 * A helper class that operate serialization on HashMap user_map in UserManager to FireBase database.
 */
final class UserManagerSerialization {
    private FireBaseDBAccess fbDb;

    UserManagerSerialization() {
        this.fbDb = new FireBaseDBAccess();
    }

    // Deserialize JSON from FireBase to a HashMap of User object, and assign it to user_map in UserManager.
    void deserialize() {
        HashMap<String, Object> user_map_temp = fbDb.retrieve("SystemUser");

        // Downcast Object key to SystemUser key.
        HashMap<String, SystemUser> user_map = new HashMap<>();
        for (String username : user_map_temp.keySet()) {
            Object object = user_map_temp.get(username);
            if (object instanceof SystemUser) {
                user_map.put(username, (SystemUser) object);
            }
        }
        UserManager.user_map = user_map;

        // REMOVE BEFORE SUBMISSION
        System.err.println("For debugging only: UserManager.user_map = " + UserManager.user_map);
    }

    //TODO
    void serialize() {
    }

    public void deleteBackup() {
        //TODO
    }

//    HashMap<String, SystemUser> loadCustom(String filename) {
//        try {
//            FileInputStream file = new FileInputStream("phase1/" + filename + ".txt");
//            ObjectInputStream object = new ObjectInputStream(file);
//            UserManagerSerialization backup = (UserManagerSerialization) object.readObject();
//            object.close();
//            file.close();
//            return backup.user_map;
//        } catch (IOException | ClassNotFoundException f) {
//            //f.printStackTrace();
//            return UserManager.user_map;
//        }
//
//    }

}


