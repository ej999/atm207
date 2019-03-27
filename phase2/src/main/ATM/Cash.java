package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * A utility class that handles cash storage, withdrawal, deposit of bills.
 */
final class Cash {

    // The denominations that accepted by the ATM.
    static final List<Integer> DENOMINATIONS;
    private static final String outputFilePath = "phase2/src/resources/alerts.txt";
    //TODO sync to firebase
    /**
     * Map denomination to quantity
     * Cash initially starts with 50 bills for every denomination.
     */
    // SortedMap is used here to make sure the denominations is in ascending order.
    static SortedMap<Integer, Integer> bills;

    static {
        DENOMINATIONS = Collections.unmodifiableList(Arrays.asList(5, 10, 20, 50));

        bills = new TreeMap<>();
        for (int d : DENOMINATIONS) {
            bills.put(d, 50);
        }
    }

    /**
     * Check the quantity of denominations
     *
     * @return true iff amount of any denomination goes below 20
     */
    static boolean isAmountBelowTwenty() {
        for (int n : bills.values()) {
            if (n < 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send an alert to alerts.txt iff isAmountBelowTwenty
     */
    static void checkDenom() {
        if (isAmountBelowTwenty()) {
            try {
                sendAlert();
            } catch (IOException e) {
                System.err.println("Failed to send alert notification about insufficient bill stock.");
            }
        }
    }

    static private void sendAlert() throws IOException {
        // Open the file for writing and write to it.
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
            //TODO improve
            out.println(bills);
        }
    }

    static void cashDeposit(Map<Integer, Integer> deposits) {
        for (int d : deposits.keySet()) {
            bills.put(d, bills.get(d) + deposits.get(d));
        }
    }

    /**
     * return the sorted mapping of denominators to the number of bills that will be withdrawn according to the
     * withdrawal amount and the inventory.
     */
    private static SortedMap<Integer, Integer> getDenominator(double amount) {
        double remainder = amount;
        SortedMap<Integer, Integer> returnBills = new TreeMap<>();
        List<Integer> denominator = new ArrayList<>(bills.keySet());

        for (int d = denominator.size() - 1; d >= 0; d--) {
            int denominatorWithdrawn = Math.min((int) Math.floor(remainder / denominator.get(d)), bills.get(denominator.get(d)));
            returnBills.put(denominator.get(d), denominatorWithdrawn);
            remainder -= denominatorWithdrawn * denominator.get(d);
        }

        return returnBills;
    }

    /**
     * In order for a withdrawal to take place, there must be enough bills to give out.
     *
     * @param amount withdrawal amount
     * @return true iff there is enough bills for amount
     */
    static boolean isThereEnoughBills(double amount) {
        Map<Integer, Integer> numberOfBills = getDenominator(amount);

        double total = 0;

        for (int denominator : numberOfBills.keySet()) {
            total += denominator * numberOfBills.get(denominator);
        }

        return amount == total;
    }

    /**
     * Cash withdrawal. The number of different bills are used in
     * withdrawal depending on the withdrawal amount and the inventory.
     * <p>
     * TODO
     */
    static void cashWithdrawal(double amount) {
        Map<Integer, Integer> numberOfBills = getDenominator(amount);

        StringBuilder print = new StringBuilder();
        int total = 0;

        for (int denominator : numberOfBills.keySet()) {
            int amountOfBills = numberOfBills.get(denominator);

            bills.put(denominator, bills.get(denominator) - amountOfBills);
            total += denominator * amountOfBills;

            print.append(amountOfBills).append(" of $").append(denominator).append("-bill, ");
        }
        checkDenom();
        System.out.println(print + "in total of " + total + " have been withdrawn.");
    }

    static void undoCashWithdrawal(double amount) {
        cashWithdrawal(-amount);
    }

}
