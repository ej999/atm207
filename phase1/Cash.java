package phase1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * A class for handling cash storage, withdrawal, deposit of $5, $10, $20, and $50 bills.
 *TODO
 * When the amount of any denomination goes below 20, your program should send an alert to a file called alerts.txt
 * that the real-life manager would read and handle by restocking the machine.
 */
class Cash {
    /*
    Map denomination to quantity
     */
    private HashMap<String, Integer> bills;
    private static final String outputFilePath = "/alerts.txt";

    Cash(ArrayList<Integer> cashList) {
        bills = new HashMap<>();
        bills.put("five", cashList.get(0));
        bills.put("ten", cashList.get(1));
        bills.put("twenty", cashList.get(2));
        bills.put("fifty", cashList.get(3));
    }

    /**
     * Check the quantity of denominations
     * @return true iff amount of any denomination goes below 20
     */
    private boolean isAmountBelowTwenty() {
        for(int n: bills.values()) {
            if (n < 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send an alert to alerts.txt iff isAmountBelowTwenty
     */
    private void checkDenom() {
        if (isAmountBelowTwenty()) {
            try {
                sendAlert();
            } catch (IOException e) {
                // do nothing?
            }
        }
    }

    private void sendAlert() throws IOException {
        // Open the file for writing and write to it.
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {
            out.println(bills);
            System.out.println("File has been written.");
        }
    }

    /*
    When bank manager restocks the machine
    cashList: [fives, tens, twenties, fifties]
     */
    void cashDeposit(ArrayList<Integer> cashList) {
        bills.put("five", bills.get("five") + cashList.get(0));
        bills.put("ten", bills.get("ten") + cashList.get(1));
        bills.put("twenty", bills.get("twenty") + cashList.get(2));
        bills.put("fifty", bills.get("fifty") + cashList.get(3));
    }

    /**
     * return a List of cash that contains the number of bills that will be withdrawn
     * according to the withdrawal amount and the inventory.
     * What is this method used for?
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
     * Update quantity of denominations.
     */
    public void cashWithdrawal(double amount) {
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
