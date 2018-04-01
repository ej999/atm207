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

import static ATM.ATM.*;

/**
 * An class that operate serialization in JSON, save and retrieve data to FireBase real-time database.
 * To import or export JSON.
 */
final class ManagersSerialization {
    private final FireBaseDBAccess fbDb;

    ManagersSerialization() {
        this.fbDb = new FireBaseDBAccess();
    }

    void deleteDatabase() {
        fbDb.delete("");
        Logger.getLogger("").info("FireBase database is set to empty");
    }

    void deserializeAll() {
        deserializeUsers();
        deserializeAccounts();
        deserializeBills();
        deserializeETransfers();
        deserializeRequests();
        deserializeBuyOffer();
        deserializeSellOffer();
    }

    // Deserialize JSON from FireBase /Users directory, and assign it to userManager.user_map
    private void deserializeUsers() {

        userManager.user_map = fbDb.retrieveMap("Users", true);

        /*
        FireBase does not saving empty array, so we re-create them:
        https://firebase.googleblog.com/2014/04/best-practices-arrays-in-firebase.html
        */
        for (String username : userManager.user_map.keySet()) {
            User user = userManager.getUser(username);
            if (user instanceof Customer && ((Customer) user).getAccountIDs() == null) {
                ((Customer) user).setAccountIDs(new ArrayList<>());
            }

            if (user instanceof Customer && ((Customer) user).getInventory() == null) {
                ((Customer) user).setInventory(new Inventory());
            }
        }

        Logger.getLogger("").info("Deserialize userManager.user_map = " + userManager.user_map);
    }

    // Deserialize JSON from FireBase /Accounts directory, and assign it to userManager.account_map
    private void deserializeAccounts() {
        accountManager.account_map = fbDb.retrieveMap("Accounts", true);

        for (String id : accountManager.account_map.keySet()) {
            Account account = accountManager.getAccount(id);
            assert account != null;
            if (account.getTransactionHistory() == null) {
                account.setTransactionHistory(new Stack<>());
            }
        }

        Logger.getLogger("").info("Deserialize accountManager.account_map = " + accountManager.account_map);
    }

    // Deserialize JSON from FireBase /Bills directory, and assign it to banknoteManager.banknotes
    private void deserializeBills() {
        HashMap<String, Integer> bills;
        bills = fbDb.retrieveMap("Bills", false);
        banknoteManager.banknotes = bills;

        Logger.getLogger("").info("Deserialize banknoteManager.banknotes = " + banknoteManager.banknotes);
    }

    // Deserialize JSON from FireBase /ETransfers directory to a List of Integer, and assign it to eTransferManager.allTransfers
    private void deserializeETransfers() {
        eTransferManager.allTransfers = fbDb.retrieveList();

        Logger.getLogger("").info("Deserialize eTransferManager.allTransfers = " + eTransferManager.allTransfers);
    }

    // Deserialize JSON from FireBase /Requests directory, and assign it to ATN.eTransferManager.requests
    private void deserializeRequests() {
        eTransferManager.requests = fbDb.retrieveMap("Requests", false);

        Logger.getLogger("").info("Deserialize eTransferManager.requests = " + eTransferManager.requests);
    }

    private void deserializeBuyOffer() {
        HashMap<String, ArrayList<HashMap>> hashMap = fbDb.retrieveMap("Trading/BuyOffer", false);
        HashMap<String, ArrayList<TradeOffer>> return_hashMap = new HashMap<>();
        for (String item : hashMap.keySet()) {
            ArrayList<TradeOffer> offerLists = new ArrayList<>();
            for (HashMap x : hashMap.get(item)) {
                Gson gson = new Gson();
                offerLists.add(gson.fromJson(x.toString(), TradeOffer.class));
            }
            return_hashMap.put(item, offerLists);
        }
        tradingSystem.buy_offers = return_hashMap;

        Logger.getLogger("").info("Deserialize tradingSystem.buy_offers = " + tradingSystem.buy_offers);
    }

    private void deserializeSellOffer() {
        HashMap<String, ArrayList<HashMap>> hashMap = fbDb.retrieveMap("Trading/SellOffer", false);
        HashMap<String, ArrayList<TradeOffer>> return_hashMap = new HashMap<>();
        for (String item : hashMap.keySet()) {
            ArrayList<TradeOffer> offerLists = new ArrayList<>();
            for (HashMap x : hashMap.get(item)) {
                Gson gson = new Gson();
                offerLists.add(gson.fromJson(x.toString(), TradeOffer.class));
            }
            return_hashMap.put(item, offerLists);
        }
        tradingSystem.sell_offers = return_hashMap;

        Logger.getLogger("").info("Deserialize tradingSystem.sell_offers = " + tradingSystem.sell_offers);
    }

    void serializeAll() {
        fbDb.saveMap(userManager.user_map, "Users");
        fbDb.saveMap(accountManager.account_map, "Accounts");
        fbDb.saveMap(banknoteManager.banknotes, "Bills");
        // Remove the current List before pushing a new one.
        fbDb.delete("ETransfers");
        fbDb.saveList(eTransferManager.allTransfers);
        fbDb.saveMap(eTransferManager.requests, "Requests");
        fbDb.saveMap(tradingSystem.sell_offers, "Trading/BuyOffer");
        fbDb.saveMap(tradingSystem.buy_offers, "Trading/SellOffer");
    }

    /**
     * A helper class that performs read and write to FireBase project.
     */
    final class FireBaseDBAccess {
        private final DatabaseReference databaseRef;

        FireBaseDBAccess() {
            initFireBase();
            // Get a reference to our database.
            databaseRef = FirebaseDatabase.getInstance().getReference("/");
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

        // To remove a child reference.
        void delete(String child) {
            CountDownLatch latch = new CountDownLatch(1);

            // Get existing child or new child will be created.
            DatabaseReference childRef = databaseRef.child(child);
            childRef.removeValueAsync();
            Logger.getLogger("").info(child + " is removed");

            latch.countDown();
            try {
                // Wait for FireBase to save record.
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // To serialize and save a Map of T
        <T extends Serializable> void saveMap(Map<String, T> item_map, String child) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item_map != null) {
                // Get existing child or new child will be created.
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
        <T extends Serializable> void saveList(List<T> item_list) {
            CountDownLatch latch = new CountDownLatch(1);
            if (item_list != null) {
                // Get existing child or new child will be created.
                DatabaseReference childRef = databaseRef.child("ETransfers");

//                childRef.removeValueAsync();

                for (T item : item_list) {
                    childRef.push().setValueAsync(item);
                }

                Logger.getLogger("").info("ETransfers" + " is serialized and saved");
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
                                T t = Json2Object(childSnapshot.getValue().toString(), classOfObj);
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
        <T extends Serializable> List<T> retrieveList() {
            // Get existing child or will bee created new child.
            DatabaseReference childRef = databaseRef.child("ETransfers");
            CountDownLatch latch = new CountDownLatch(1);
            List<T> t_list = new ArrayList<>();

            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        try {
                            @SuppressWarnings("unchecked")
                            Class<T> classOfT = (Class<T>) Class.forName((String) ((HashMap) childSnapshot.getValue()).get("type"));
                            t_list.add(Json2Object(childSnapshot.getValue().toString().replace(" ", ""), classOfT));
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
            return t_list;
        }

        // Helper method: convert JSON to class
        <T extends Serializable> T Json2Object(String json, Class<T> classOfT) {
            Gson gson = new Gson();
            return gson.fromJson(json, classOfT);
        }
    }
}


