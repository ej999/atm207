package ATM;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * A helper class for ManagersSerialization that allow read and write to FireBase project.
 */
final class FireBaseDBAccess {
    private static boolean initialized = false;
    private static DatabaseReference databaseRef;

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