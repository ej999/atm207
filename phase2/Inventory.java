package phase2;

import java.util.HashMap;

//Where users can store non-money goods. Stored in a HashMap with the string name of the good (Steel, Copper,
//etc.), with its value being the amount of it.
public class Inventory {
    HashMap<String, Integer> storage = new HashMap<>();
    Inventory(){
    }

    Inventory(HashMap<String, Integer> storage){
        this.storage = storage;
    }

    boolean itemExists(String item){
        return storage.containsKey(item);
    }

    Integer itemAmount(String item){
        return storage.get(item);
    }

    void addItem(String item, Integer amount){
        if (storage.containsKey(item)){
            storage.put(item, storage.get(item) + amount);
        }
        else {
            storage.put(item, amount);
        }
    }
}
