package ATM;

import java.util.ArrayList;
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
        HashMap<String, Object> user_map_temp = fbDb.retrieveAll("Users");

        // Downcast Object key to User key.
        HashMap<String, User> user_map = new HashMap<>();
        for (String username : user_map_temp.keySet()) {
            Object object = user_map_temp.get(username);
            if (object instanceof User) {
                user_map.put(username, (User) object);
            }
        }
        UserManager.user_map = user_map;

        HashMap<String, Object> account_list_temp = fbDb.retrieveAll("Accounts");

        // Downcast Object type to Account type, and convert to ArrayList.
        ArrayList<Account> account_list = new ArrayList<>();
        for (String username : account_list_temp.keySet()) {
            Object object = account_list_temp.get(username);
            if (object instanceof Account) {
                account_list.add((Account) object);
            }
        }
        AccountManager.account_list = account_list;

        // REMOVE BEFORE SUBMISSION
        System.err.println("For debugging only: UserManager.user_map = " + UserManager.user_map);
        System.err.println("For debugging only: AccountManager.account_list = " + AccountManager.account_list);
    }

    // All the data structures stored in suffix-Manager classes will be serialize to JSON after a action is performed by User.
    void serialize() {
        fbDb.saveAll(UserManager.user_map, "Users");
        fbDb.saveAll(AccountManager.account_list, "Accounts");
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


