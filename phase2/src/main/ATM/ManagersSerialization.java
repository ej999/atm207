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
 * An observer class that operate serialization in real-time, save and retrieve data to FireBase database.
 */
final class ManagersSerialization {
    static void deleteDatabase() {
        FireBaseDBAccess.save(0, "", "");

        ATM.log.info("FireBase database is set to empty.");
    }

    void deserialize() {
        // Deserialize JSON from /Users directory in FireBase to a HashMap of User, and assign it to user_map in UserManager.
        HashMap<String, Object> user_map_temp = FireBaseDBAccess.retrieveAll("Users", true);

        HashMap<String, User> user_map = new HashMap<>();
        for (String username : user_map_temp.keySet()) {
            Object object = user_map_temp.get(username);
            user_map.put(username, (User) object);
        }
        UserManager.user_map = user_map;

        // Deserialize JSON from /Accounts directory in FireBase to a HashMap of Account, and assign it to account_map in AccountManager.
        HashMap<String, Object> account_list_temp = FireBaseDBAccess.retrieveAll("Accounts", true);
        HashMap<String, Account> account_map = new HashMap<>();
        for (String n : account_list_temp.keySet()) {
            Object object = account_list_temp.get(n);
            account_map.put(n, (Account) object);
        }
        AccountManager.account_map = account_map;

        // Deserialize JSON from /Bills directory in FireBase to a HashMap of Integer, and assign it to ATMBills in Cash.
        HashMap<String, Object> bills_temp = FireBaseDBAccess.retrieveAll("Bills", false);
        // Downcast Object value to Integer value.
        HashMap<String, Integer> bills = new HashMap<>();
        for (String n : bills_temp.keySet()) {
            Object object = bills_temp.get(n);
            bills.put(n, Math.toIntExact((Long) object));
        }
        Cash.ATMBills = bills;


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

        ATM.log.info("Deserialize UserManager.user_map = " + UserManager.user_map);
        ATM.log.info("Deserialize AccountManager.account_map = " + AccountManager.account_map);
        ATM.log.info("Deserialize Cash.ATMBill = " + Cash.ATMBills);
    }

    void serializeAll() {
        FireBaseDBAccess.saveAll(UserManager.user_map, "Users");
        FireBaseDBAccess.saveAll(AccountManager.account_map, "Accounts");
        FireBaseDBAccess.saveAll(Cash.ATMBills, "Bills");

        ATM.log.info("ATMBills is serialized and saved.");
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
     * A helper class that allow read and write to FireBase project.
     */
    static final class FireBaseDBAccess {
        static private DatabaseReference databaseRef;


        static {
            initFireBase();
        }

        static private void initFireBase() {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        static void save(Object item, String child, String key) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child(child).child(key);


                childRef.setValue(item, (error, ref) -> latch.countDown());

                try {
                    // Wait for FireBase to save record.
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        static void saveAll(HashMap item_map, String child) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item_map != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child(child);

                childRef.setValueAsync(item_map);
                latch.countDown();

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Return a HashMap of all the child items as their objects in database.
         */
        static HashMap<String, Object> retrieveAll(String child, boolean toClass) {
            CountDownLatch latch = new CountDownLatch(1);
            // Get existing child or will bee created new child.
            DatabaseReference childRef = databaseRef.child(child);

            HashMap<String, Object> object_map = new HashMap<>();

            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        try {
                            if (toClass) {
                                // Convert JSON to class
                                Class classOfObj = Class.forName((String) ((HashMap) childSnapshot.getValue()).get("type"));
                                Gson gson = new Gson();
                                Object object = gson.fromJson(childSnapshot.getValue().toString(), classOfObj);

                                object_map.put(childSnapshot.getKey(), object);
                            } else {
                                object_map.put(childSnapshot.getKey(), childSnapshot.getValue());
                            }
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
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return object_map;
        }

//        void push(Object item, String child) {
//            if (item != null) {
//                // Get existing child or will bee created new child.
//                DatabaseReference childRef = databaseRef.child(child);
//
//                childRef.push().setValueAsync(item);
//                System.out.println("Record pushed!");
//
//                try {
//                    // Wait for FireBase to save record.
//                    latch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}


