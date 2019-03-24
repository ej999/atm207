package ATM;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * A helper class that allow read and write to project's FireBase database.
 */
class FireBaseDBAccess {
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
            databaseRef = FirebaseDatabase.getInstance().getReference("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void save(Object item, String child) {
        if (item != null) {
            // Get existing child or will bee created new child.
            DatabaseReference childRef = databaseRef.child(child);

            // Using countDownLatch here to prevent the JVM from exiting before the thread is still running.
            CountDownLatch latch = new CountDownLatch(1);

            childRef.setValue(item, (error, ref) -> {
                System.out.println("Record saved!");
                latch.countDown();
            });

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

    void retrieve(String child) {
        // Get existing child or will bee created new child.
        DatabaseReference childRef = databaseRef.child(child);

        // Using countDownLatch here to prevent the JVM from exiting before the thread is still running.
        CountDownLatch latch = new CountDownLatch(1);

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String ob = snapshot.getValue().toString();
                System.out.println(ob);

                Gson gson = new Gson();
                Item item = gson.fromJson(ob, Item.class);
                System.out.println(item);
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
                latch.countDown();
            }
        });


        try {
            // Wait for FireBase to save record.
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}