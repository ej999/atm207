package phase2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A utility class that handles cash storage, withdrawal, deposit of $5, $10, $20, and $50 bills.
 */
final class Cash {
    /**
     * Map denomination to quantity
     * Cash initially starts with fifty bills of every denomination
     */
    private static final HashMap<String, Integer> bills = new HashMap<String, Integer>() {
        {
            put("five", 50);
            put("ten", 50);
            put("twenty", 50);
            put("fifty", 50);
        }
    };
    private static final String outputFilePath = "phase1/alerts.txt";

    /**
     * Check the quantity of denominations
     *
     * @return true iff amount of any denomination goes below 20
     */
    static private boolean isAmountBelowTwenty() {
        for (int n : bills.values()) {
            if (n < 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * //TODO look into this
     * Send an alert to alerts.txt iff isAmountBelowTwenty
     */
    static private void checkDenom() {
        if (isAmountBelowTwenty()) {
            try {
                sendAlert();
            } catch (IOException e) {
                // do nothing?
            }
        }
    }

    static private void sendAlert() throws IOException {
        // Open the file for writing and write to it.
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
            out.println(bills);
        }
    }

    /*
    When bank manager restocks the machine
    cashList: [fives, tens, twenties, fifties]
     */
    static void cashDeposit(ArrayList<Integer> cashList) {
        bills.put("five", bills.get("five") + cashList.get(0));
        bills.put("ten", bills.get("ten") + cashList.get(1));
        bills.put("twenty", bills.get("twenty") + cashList.get(2));
        bills.put("fifty", bills.get("fifty") + cashList.get(3));
    }

    /**
     * return a List [fifty, twenty, ten, five] of cash that contains the number of bills that will be withdrawn
     * according to the withdrawal amount and the inventory.
     */
    private static ArrayList<Integer> getDenominator(double amount) {
        double remainder = amount;

        int fiftyWithdrawn = Math.min((int) Math.floor(remainder / 50), bills.get("fifty"));
        remainder -= fiftyWithdrawn * 50;

        int twentyWithdrawn = Math.min((int) Math.floor(remainder / 20), bills.get("twenty"));
        remainder -= twentyWithdrawn * 20;

        int tenWithdrawn = Math.min((int) Math.floor(remainder / 10), bills.get("ten"));
        remainder -= tenWithdrawn * 10;

        int fiveWithdrawn = Math.min((int) Math.floor(remainder / 5), bills.get("five"));

        return new ArrayList<>(Arrays.asList(fiftyWithdrawn, twentyWithdrawn, tenWithdrawn, fiveWithdrawn));
    }

    /**
     * In order for a withdrawal to take place, there must be enough bills to give out.
     *
     * @param amount withdrawal amount
     * @return true iff there is enough bills for amount
     */
    static boolean isThereEnoughBills(double amount) {
        ArrayList<Integer> numberOfBills = getDenominator(amount);
        double total = numberOfBills.get(0) * 50 + numberOfBills.get(1) * 20 + numberOfBills.get(2) * 10 +
                numberOfBills.get(3) * 5;
        return amount == total;
    }

    /**
     * Cash withdrawal. The number of different bills are used in
     * withdrawal depending on the withdrawal amount and the inventory.
     *
     * TODO
     */
    static void cashWithdrawal(double amount) {
        ArrayList<Integer> denominator = getDenominator(amount);

        bills.put("fifty", bills.get("fifty") - denominator.get(0));

        bills.put("twenty", bills.get("twenty") - denominator.get(1));

        bills.put("ten", bills.get("ten") - denominator.get(2));

        bills.put("five", bills.get("five") - denominator.get(3));

        checkDenom();
        int totalAmount = denominator.get(0) * 50 + denominator.get(1) * 20 + denominator.get(2) * 10 + denominator.get(3) * 5;
        //TODO
        System.out.println("\nTotal amount of $" + totalAmount + ": " + denominator.get(0) + " fifty-dollar bills, " +
                denominator.get(1) + " twenty-dollar bills, " + denominator.get(2) + " ten-dollar bills, " + denominator.get(3) +
                " five-dollar bills have be withdrawn. ");
        System.out.println("Please note that the actual withdrawal amount may be differ " +
                "due to the fact that five-dollar note is the lowest denomination");
    }

    static void undoCashWithdrawal(double amount) {
        cashWithdrawal(-amount);
    }

}
