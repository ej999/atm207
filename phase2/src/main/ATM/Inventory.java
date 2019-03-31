package ATM;

import java.util.ArrayList;
import java.util.HashMap;


//Where users can store non-money goods. Stored in a HashMap with the string name of the good (Steel, Copper,
//etc.), with its value being the amount of it.
class Inventory {
    private final HashMap<String, Integer> storage;

    Inventory() {
        this.storage = new HashMap<>();
    }

    @SuppressWarnings("unused") // serialization property
    public HashMap<String, Integer> getStorage() {
        return storage;
    }

    private boolean itemExists(String item) {
        return storage.containsKey(item);
    }

    Integer itemAmount(String item) {

        if (itemExists(item)) {
            return storage.get(item);
        } else {
            return 0;
        }
    }

    void depositItem(String item, Integer amount) {
        if (storage.containsKey(item)) {
            storage.put(item, storage.get(item) + amount);
        } else {
            storage.put(item, amount);
        }
        String returned = "Deposited " + amount + " integer unit of " + item;
        System.out.println(returned);
    }

    void withdrawItem(String item, Integer amount) {
        if (storage.containsKey(item)) {
            if (storage.get(item) >= amount)
                storage.put(item, storage.get(item) - amount);
        }
    }

    ArrayList<String> viewInventory() {
        ArrayList<String> returned = new ArrayList<>();

        for (String key : storage.keySet()) {
            returned.add(storage.get(key) + " unit of " + key);
        }
        return returned;
    }
}
