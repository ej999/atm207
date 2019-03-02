package phase1;
import java.util.HashMap;
import java.util.Map;
//Import ATM


public class BankManager extends Employees {

    public BankManager(){

    }

    //Assume ATM stores bills as HashMap
    public void addToTill(HashMap<Integer, Integer> bills, HashMap<Integer, Integer> ATM){
        for (Map.Entry<Integer, Integer> denom : bills.entrySet()) {
            ATM.replace(denom.getKey(), denom.getValue() + ATM.get(denom.getKey()));
        }
    }

}
