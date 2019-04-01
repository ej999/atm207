package ATM;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * An class that operate serialization in JSON, save and retrieve data to FireBase real-time database.
 * To import or export JSON.
 */
final class ManagersSerialization {
    private FireBaseDBAccess fbDb;

    ManagersSerialization() {
        this.fbDb = new FireBaseDBAccess("/" +
                "");
    }

    void deleteDatabase() {
        fbDb.save(0, "", "");
        Logger.getLogger("").info("FireBase database is set to empty");
    }

    void deserializeAll() {
        deserializeUsers();
        deserializeAccounts();
        deserializeBills();
        deserializeETransfers();
        deserializeRequests();
    }

    // Deserialize JSON from FireBase /Users directory, and assign it to ATM.userManager.user_map
    private void deserializeUsers() {

        ATM.userManager.user_map = fbDb.retrieveMap("Users", true);

        /*
        FireBase does not saving empty array, so we re-create them:
        https://firebase.googleblog.com/2014/04/best-practices-arrays-in-firebase.html
        */
        for (String username : ATM.userManager.user_map.keySet()) {
            User user = ATM.userManager.getUser(username);
            if (user instanceof Customer && ((Customer) user).getAccountIDs() == null) {
                ((Customer) user).accountIDs = new ArrayList<>();
            }
        }

        Logger.getLogger("").info("Deserialize ATM.userManager.user_map = " + ATM.userManager.user_map);
    }

    // Deserialize JSON from FireBase /Accounts directory, and assign it to ATM.userManager.account_map
    private void deserializeAccounts() {
        ATM.accountManager.account_map = fbDb.retrieveMap("Accounts", true);

        for (String id : ATM.accountManager.account_map.keySet()) {
            Account account = ATM.accountManager.getAccount(id);
            if (account.getTransactionHistory() == null) {
                account.setTransactionHistory(new Stack<>());
            }
        }

        Logger.getLogger("").info("Deserialize ATM.accountManager.account_map = " + ATM.accountManager.account_map);
    }

    // Deserialize JSON from FireBase /Bills directory, and assign it to ATM.banknoteManager.banknotes
    private void deserializeBills() {
        ATM.banknoteManager.banknotes = fbDb.retrieveMap("Bills", false);

        Logger.getLogger("").info("Deserialize ATM.banknoteManager.banknotes = " + ATM.banknoteManager.banknotes);
    }

    // Deserialize JSON from FireBase /ETransfers directory to a List of Integer, and assign it to ATM.eTransferManager.allTransfers
    private void deserializeETransfers() {
        ATM.eTransferManager.allTransfers = fbDb.retrieveList("ETransfers", true);

        Logger.getLogger("").info("Deserialize ATM.eTransferManager.allTransfers = " + ATM.eTransferManager.allTransfers);
    }

    // Deserialize JSON from FireBase /Requests directory, and assign it to ATN.eTransferManager.requests
    private void deserializeRequests() {
        ATM.eTransferManager.requests = fbDb.retrieveMap("Requests", false);

        Logger.getLogger("").info("Deserialize ATM.eTransferManager.requests = " + ATM.eTransferManager.requests);
    }


    void serializeAll() {
        fbDb.saveMap(ATM.userManager.user_map, "Users");
        fbDb.saveMap(ATM.accountManager.account_map, "Accounts");
        fbDb.saveMap(ATM.banknoteManager.banknotes, "Bills");
        fbDb.saveList(ATM.eTransferManager.allTransfers, "ETransfers");
        fbDb.saveMap(ATM.eTransferManager.requests, "Requests");
    }

    /**
     * A helper class that performs read and write to FireBase project.
     */
    final class FireBaseDBAccess {
        private DatabaseReference databaseRef;

        FireBaseDBAccess(String path) {
            initFireBase();
            // Get a reference to our database.
            databaseRef = FirebaseDatabase.getInstance().getReference(path);
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // To serialize and save a single T
        <T extends Serializable> void save(T item, String child, String key) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child(child).child(key);

                childRef.setValue(item, (error, ref) -> latch.countDown());
                Logger.getLogger("").info(key + " of " + child + " is serialized and saved");
                try {
                    // Wait for FireBase to save record.
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // To serialize and save a Map of T
        <T extends Serializable> void saveMap(Map<String, T> item_map, String child) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item_map != null) {
                // Get existing child or new child will be created.
                // TODO: 2019-03-29 unwrap t variable into common type before serializing, and vise versa.
                DatabaseReference childRef = databaseRef.child(child);
                childRef.setValueAsync(item_map);

                Logger.getLogger("").info(child + " is serialized and saved");
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // To serialize and save a List of T
        <T extends Serializable> void saveList(List<T> item_list, String child) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item_list != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child(child);

//                childRef.removeValueAsync();

                for (T item : item_list) {
                    childRef.push().setValueAsync(item);
                }

                Logger.getLogger("").info(child + " is serialized and saved");
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Return a HashMap of all the child items as T in database
        <T extends Serializable> HashMap<String, T> retrieveMap(String child, boolean toClass) {
            CountDownLatch latch = new CountDownLatch(1);
            // Get existing child or will bee created new child.
            DatabaseReference childRef = databaseRef.child(child);

            HashMap<String, T> t_map = new HashMap<>();

            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        try {
                            if (toClass) {
                                // Convert JSON to class
                                @SuppressWarnings("unchecked")
                                Class<T> classOfObj = (Class<T>) Class.forName((String) ((HashMap) childSnapshot.getValue()).get("type"));
                                Gson gson = new Gson();
                                T t = gson.fromJson(childSnapshot.getValue().toString(), classOfObj);
                                t_map.put(childSnapshot.getKey(), t);
                            } else {
                                @SuppressWarnings("unchecked")
                                T t = (T) childSnapshot.getValue();
                                t_map.put(childSnapshot.getKey(), t);
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
            return t_map;
        }

        // Return a List of all the child items as T in database
        <T extends Serializable> List<T> retrieveList(String child, boolean toClass) {
            // Get existing child or will bee created new child.
            DatabaseReference childRef = databaseRef.child(child);
            CountDownLatch latch = new CountDownLatch(1);
            List<T> t_list = new ArrayList<>();

            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        try {
                            if (toClass) {
                                @SuppressWarnings("unchecked")
                                Class<T> classOfT = (Class<T>) Class.forName((String) ((HashMap) childSnapshot.getValue()).get("type"));
                                t_list.add(Json2Object(childSnapshot.getValue().toString().replace(" ", ""), classOfT));
                            } else {
                                @SuppressWarnings("unchecked")
                                T t = (T) childSnapshot.getValue();
                                System.out.println("OUCH");
                                t_list.add(t);
                            }
                        } catch (ClassNotFoundException e) {
                            e.getStackTrace();
                            System.out.println("OUTH");
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
            return t_list;
        }

        // Helper method: convert JSON to class
        <T extends Serializable> T Json2Object(String json, Class<T> classOfT) {
            Gson gson = new Gson();
            return gson.fromJson(json, classOfT);
        }
    }
}


