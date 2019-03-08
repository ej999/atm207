package phase1;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class for handling cash storage, withdrawal, deposit of $5, $10, $20, and $50 bills.
 * <p>
 *TODO
 * When the amount of any denomination goes below 20, your program should send an alert to a file called alerts.txt
 * that the real-life manager would read and handle by restocking the machine.
 */
class Cash {
    private HashMap<String, Integer> bills;

    Cash(ArrayList<Integer> cashList) {
        bills = new HashMap<>();
        bills.put("five", cashList.get(0));
        bills.put("ten", cashList.get(1));
        bills.put("twenty", cashList.get(2));
        bills.put("fifty", cashList.get(3));
    }

    void cashDeposit(ArrayList<Integer> cashList) {
        bills.put("five", bills.get("five") + cashList.get(0));
        bills.put("ten", bills.get("ten") + cashList.get(1));
        bills.put("twenty", bills.get("twenty") + cashList.get(2));
        bills.put("fifty", bills.get("fifty") + cashList.get(3));
    }

    /**
     * return a List of cash that contains the number of bills that will be withdrawn
     * according to the withdrawal amount and the inventory.
     */
    ArrayList<Integer> verifyCashWithdrawal(double amount) {
        double remainder = amount;
        ArrayList<Integer> cashList = new ArrayList<>();

        int fiftyWithdrawn = Math.min((int) Math.floor(remainder / 50), bills.get("fifty"));
        cashList.add(fiftyWithdrawn);
        remainder -= fiftyWithdrawn * 50;

        int twentyWithdrawn = Math.min((int) Math.floor(remainder / 20), bills.get("twenty"));
        cashList.add(twentyWithdrawn);
        remainder -= twentyWithdrawn * 20;

        int tenWithdrawn = Math.min((int) Math.floor(remainder / 10), bills.get("ten"));
        cashList.add(tenWithdrawn);
        remainder -= tenWithdrawn * 10;

        int fiveWithdrawn = Math.min((int) Math.floor(remainder / 5), bills.get("five"));
        cashList.add(fiveWithdrawn);

        return cashList;
    }

    /**
     * Cash withdrawal. The number of different bills are used in
     * withdrawal depending on the withdrawal amount and the inventory.
     */
    void cashWithdrawal(double amount) {
        double remainder = amount;

        // The number of a specific bill withdrawn should be the smaller integer of either the amount of the
        // specific bill that needed to be withdrawn or the inventory of that bill.
        int fiftyWithdrawn = Math.min((int) Math.floor(remainder / 50), bills.get("fifty"));
        bills.put("fifty", bills.get("fifty") - fiftyWithdrawn);
        remainder -= fiftyWithdrawn * 50;

        int twentyWithdrawn = Math.min((int) Math.floor(remainder / 20), bills.get("twenty"));
        bills.put("twenty", bills.get("twenty") - twentyWithdrawn);
        remainder -= twentyWithdrawn * 20;

        int tenWithdrawn = Math.min((int) Math.floor(remainder / 10), bills.get("ten"));
        bills.put("ten", bills.get("ten") - tenWithdrawn);
        remainder -= tenWithdrawn * 10;

        int fiveWithdrawn = Math.min((int) Math.floor(remainder / 5), bills.get("five"));
        bills.put("five", bills.get("five") - fiveWithdrawn);
    }

    public int getFiveDollarBill() {
        return bills.get("five");
    }

    public void setFiveDollarBill(int fiveDollarBill) {
        bills.put("five", fiveDollarBill);
    }

    public int getTenDollarBill() {
        return bills.get("ten");
    }

    public void setTenDollarBill(int tenDollarBill) {
        bills.put("ten", tenDollarBill);
    }

    public int getTwentyDollarBill() {
        return bills.get("twenty");
    }

    public void setTwentyDollarBill(int twentyDollarBill) {
        bills.put("twenty", twentyDollarBill);
    }

    public int getFiftyDollarBill() {
        return bills.get("fifty");
    }

    public void setFiftyDollarBill(int fiftyDollarBill) {
        bills.put("fifty", fiftyDollarBill);
    }
}
