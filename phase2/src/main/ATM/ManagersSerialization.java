package ATM;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;

/**
 * A helper class that operate serialization on UserManager and AccountManager, and save data to FireBase database.
 */
final class ManagersSerialization {
    private FireBaseDBAccess fbDb;

    ManagersSerialization() {
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
        // Downcast Object value to Account value.
        HashMap<String, Account> account_map = new HashMap<>();
        for (String n : account_list_temp.keySet()) {
            Object object = account_list_temp.get(n);
            if (object instanceof Account) {
                account_map.put(n, (Account) object);
            }
        }
        AccountManager.account_map = account_map;


//        HashMap<String, Object> bills_temp = fbDb.retrieveAll("Bills");
//        // Downcast Object value to Integer value.
//        HashMap<String, Integer> bills = new HashMap<>();
//        for (String n : bills_temp.keySet()) {
//            Object object = bills_temp.get(n);
//            if (object instanceof Integer) {
//                bills.put(n, (Integer) object);
//            }
//        }
//        Cash.ATMBills = bills;


        // FireBase has no native support for arrays, so we re-create these variables: https://firebase.googleblog.com/2014/04/best-practices-arrays-in-firebase.html
        for (String id : AccountManager.account_map.keySet()) {
            Account account = AccountManager.getAccount(id);
            if (account.getTransactionHistory() == null) {
                account.transactionHistory = new Stack<Transaction>();
            }
        }

        for (String username : UserManager.user_map.keySet()) {
            User user = UserManager.getUser(username);
            if (user instanceof Customer && ((Customer) user).getAccounts() == null) {
                ((Customer) user).accounts = new ArrayList<>();
            }
        }

        // REMOVE BEFORE SUBMISSION
        System.err.println("DEBUGGING: UserManager.user_map = " + UserManager.user_map);
        System.err.println("DEBUGGING: AccountManager.user_map = " + AccountManager.account_map);
    }

    // All the data structures stored in suffix-Manager classes will be serialize to JSON after a action is performed by User.
    void serialize() {
        fbDb.saveAll(UserManager.user_map, "Users");
        fbDb.saveAll(AccountManager.account_map, "Accounts");
        fbDb.saveAll(Cash.ATMBills, "Bills/Bill");
        System.err.print("Serialized data saved. ");
    }

    void deleteDatabase() {
        fbDb.save(0, "", "");
    }

//    HashMap<String, User> loadCustom(String filename) {
//        try {
//            FileInputStream file = new FileInputStream("phase2/src/resources/" + filename + ".txt");
//            ObjectInputStream object = new ObjectInputStream(file);
//            ManagersSerialization backup = (ManagersSerialization) object.readObject();
//            object.close();
//            file.close();
//            return backup.user_map;
//        } catch (IOException | ClassNotFoundException f) {
//            //f.printStackTrace();
//            return UserManager.user_map;
//        }
//
//    }


    /**
     * A helper class for ManagersSerialization that allow read and write to FireBase project.
     */
    final class FireBaseDBAccess {
        private boolean initialized = false;
        private DatabaseReference databaseRef;

        FireBaseDBAccess() {
            if (!initialized) {
                initFireBase();
            }
        }

        private void initFireBase() {
            try {
                // FireBase private key generated when creating service account.
                FileInputStream serviceAccount = new FileInputStream("./phase2/src/resources/serviceAccountKey.json");

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        // FireBase project URL
                        .setDatabaseUrl("https://csc207-atm-project.firebaseio.com/")
                        .build();

                FirebaseApp.initializeApp(options);

                // Get a reference to our database.
                databaseRef = FirebaseDatabase.getInstance().getReference("/");

                initialized = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void save(Object item, String child, String key) {
            if (item != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child(child).child(key);

                // Using countDownLatch here to prevent the JVM from exiting before the thread is still running.
                CountDownLatch latch = new CountDownLatch(1);

                childRef.setValue(item, (error, ref) -> latch.countDown());

                try {
                    // Wait for FireBase to save record.
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void saveAll(HashMap item_map, String child) {
            if (item_map != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child(child);

                // Using countDownLatch here to prevent the JVM from exiting before the thread is still running.
                CountDownLatch latch = new CountDownLatch(1);

                childRef.setValueAsync(item_map);
                latch.countDown();

                try {
                    // Wait for FireBase to save record.
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void push(Object item, String child) {
            if (item != null) {
                // Get existing child or will bee created new child.
                DatabaseReference childRef = databaseRef.child(child);

                // Using countDownLatch here to prevent the JVM from exiting before the thread is still running.
                CountDownLatch latch = new CountDownLatch(1);

                childRef.push().setValueAsync(item);
                System.out.println("Record pushed!");

                try {
                    // Wait for FireBase to save record.
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Return a HashMap of all the child items as their objects in database.
         */
        HashMap<String, Object> retrieveAll(String child) {
            // Get existing child or will bee created new child.
            DatabaseReference childRef = databaseRef.child(child);

            // Using countDownLatch here to prevent the JVM from exiting before the thread is still running.
            CountDownLatch latch = new CountDownLatch(1);

            HashMap<String, Object> object_map = new HashMap<>();

            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        try {
                            Class classOfObj = Class.forName((String) ((HashMap) childSnapshot.getValue()).get("type"));
                            Object object = json2object(childSnapshot.getValue(), classOfObj);
                            object_map.put(childSnapshot.getKey(), object);
                        } catch (ClassNotFoundException e) {
                            e.getStackTrace();
                        }
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });

            try {
                // Wait for FireBase to save record.
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return object_map;
        }

        // Helper method that convert object from JSON using Gson.
        private <T> T json2object(Object ob, Class<T> classOfT) {
            Gson gson = new Gson();
            return gson.fromJson(ob.toString(), classOfT);
        }
    }

}


