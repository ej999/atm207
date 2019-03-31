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
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * An class that operate serialization in JSON, save and retrieve data to FireBase real-time database.
 * To import or export JSON.
 */
final class ManagersSerialization {
    FireBaseDBAccess fbDb;

    public ManagersSerialization() {
        this.fbDb = new FireBaseDBAccess();
    }

    void deleteDatabase() {
        fbDb.save(0, "", "");

        Logger.getLogger("Custom").info("FireBase database is set to empty");
    }

    void deserialize() {
        // Deserialize JSON from /Users directory in FireBase to a HashMap of User, and assign it to user_map in ATM.userManager.
        HashMap<String, Object> user_map_temp = fbDb.retrieveAll("Users", true);
        HashMap<String, User> user_map = new HashMap<>();
        for (String username : user_map_temp.keySet()) {
            Object object = user_map_temp.get(username);
            user_map.put(username, (User) object);
        }
        ATM.userManager.user_map = user_map;

        for (String username : ATM.userManager.user_map.keySet()) {
            User user = ATM.userManager.getUser(username);
            if (user instanceof Customer && ((Customer) user).getAccounts() == null) {
                ((Customer) user).accounts = new ArrayList<>();
            }
        }

        // Deserialize JSON from /Accounts directory in FireBase to a HashMap of Account, and assign it to account_map in ATM.accountManager.
        HashMap<String, Object> account_list_temp = fbDb.retrieveAll("Accounts", true);
        HashMap<String, Account> account_map = new HashMap<>();
        for (String n : account_list_temp.keySet()) {
            Object object = account_list_temp.get(n);
            account_map.put(n, (Account) object);
        }
        ATM.accountManager.account_map = account_map;

        // FireBase has no native support for arrays, so we re-create these variables: https://firebase.googleblog.com/2014/04/best-practices-arrays-in-firebase.html
        for (String id : ATM.accountManager.account_map.keySet()) {
            Account account = ATM.accountManager.getAccount(id);

            if (account.getTransactionHistory() == null) {
                account.setTransactionHistory(new Stack<>());
            }
        }


        // Deserialize JSON from /Bills directory in FireBase to a HashMap of Integer, and assign it to ATMBills in Cash.
        HashMap<String, Object> bills_temp = fbDb.retrieveAll("Bills", false);
        // Downcast Object value to Integer value.
        HashMap<String, Integer> bills = new HashMap<>();
        for (String n : bills_temp.keySet()) {
            Object object = bills_temp.get(n);
            bills.put(n, Math.toIntExact((Long) object));
        }
        Cash.ATMBills = bills;

        List<Object> transfers_temp = fbDb.retrieveList("Etransfers", false);
        List<ETransfer> transfers = new ArrayList<>();
        for (Object o: transfers_temp){
            transfers.add((ETransfer) o);
        }
        ATM.eTransferManager.allTransfers = transfers;

        HashMap<String, Object> requests_temp = fbDb.retrieveAll("Requests", false);
        HashMap<String, HashMap<String, Double>> requests = new HashMap<>();
        for (String k: requests_temp.keySet()){
            Object obj = requests_temp.get(k);
            requests.put(k, (HashMap<String, Double>)obj);
        }
        ATM.eTransferManager.requests = requests;


        Logger.getLogger("Custom").info("Deserialize ATM.userManager.user_map = " + ATM.userManager.user_map);
        Logger.getLogger("Custom").info("Deserialize ATM.accountManager.account_map = " + ATM.accountManager.account_map);
        Logger.getLogger("Custom").info("Deserialize Cash.ATMBill = " + Cash.ATMBills);
    }

    void serializeAll() {
        fbDb.saveAll(ATM.userManager.user_map, "Users");
        fbDb.saveAll(ATM.accountManager.account_map, "Accounts");
        fbDb.saveAll(Cash.ATMBills, "Bills");

        fbDb.saveList(ATM.eTransferManager.allTransfers, "Etransfers");
        fbDb.saveAll(ATM.eTransferManager.requests, "Requests");

        Logger.getLogger("Custom").info("ATMBills is serialized and saved");
    }

    /**
     * A helper class that allow read and write to FireBase project.
     */
    final class FireBaseDBAccess {
        private DatabaseReference databaseRef;


        FireBaseDBAccess() {
            initFireBase();
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
                databaseRef = FirebaseDatabase.getInstance().getReference("/debugging");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void save(Object item, String child, String key) {
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

        void saveAll(HashMap item_map, String child) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item_map != null) {
                // Get existing child or new child will be created.
                // TODO: 2019-03-29 unwrap object variable into common type before serializing, and vise versa.
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

        void saveList(List item_list, String child) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item_list != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child(child);

                for (Object item : item_list) {
                    childRef.push().setValueAsync(item);
                }
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
        HashMap<String, Object> retrieveAll(String child, boolean toClass) {
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

        List<Object> retrieveList(String child, boolean toClass) {
            CountDownLatch latch = new CountDownLatch(1);
            // Get existing child or will bee created new child.
            DatabaseReference childRef = databaseRef.child(child);

            List<Object> objects = new ArrayList<>();

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
                                objects.add(object);
                            } else {
                                objects.add(childSnapshot.getValue());
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
            return objects;
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


