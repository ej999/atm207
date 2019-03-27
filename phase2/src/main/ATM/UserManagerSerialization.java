package ATM;

import java.util.HashMap;

/**
 * A helper class that operate serialization on UserManager and AccountManager, and save data to FireBase database.
 */
final class UserManagerSerialization {
    private FireBaseDBAccess fbDb;

    UserManagerSerialization() {
        this.fbDb = new FireBaseDBAccess();
    }

    // Deserialize JSON from FireBase to a HashMap of User object, and assign it to user_map in UserManager.
    void deserialize() {
        HashMap<String, Object> user_map_temp = fbDb.retrieveAll("Users");

        // Downcast Object value to User value.
        HashMap<String, User> user_map = new HashMap<>();
        for (String username : user_map_temp.keySet()) {
            Object object = user_map_temp.get(username);
            if (object instanceof User) {
                user_map.put(username, (User) object);
            }
        }
        UserManager.user_map = user_map;


        HashMap<String, Object> account_list_temp = fbDb.retrieveAll("Accounts");

        // Downcast String key to Integer key, and Object value to Account value.
        HashMap<String, Account> account_map = new HashMap<>();
        for (String n : account_list_temp.keySet()) {
            Object object = account_list_temp.get(n);
            if (object instanceof Account) {
                account_map.put(n, (Account) object);
            }
        }
        AccountManager.account_map = account_map;

        // REMOVE BEFORE SUBMISSION
        System.err.println("DEBUGGING: UserManager.user_map = " + UserManager.user_map);
        System.err.println("DEBUGGING: AccountManager.user_map = " + AccountManager.account_map);
    }

    // All the data structures stored in suffix-Manager classes will be serialize to JSON after a action is performed by User.
    void serialize() {
        fbDb.saveAll(UserManager.user_map, "Users");
        fbDb.saveAll(AccountManager.account_map, "Accounts");
        System.err.print("Serialized data saved. ");
    }

    public void deleteDatabase() {
        fbDb.save(0, "", "");
    }

//    HashMap<String, User> loadCustom(String filename) {
//        try {
//            FileInputStream file = new FileInputStream("phase2/src/resources/" + filename + ".txt");
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


