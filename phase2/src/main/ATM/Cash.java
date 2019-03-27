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
    /**
     * Map denomination to quantity
     * Cash initially starts with 50 bills for every denomination.
     */
    static final HashMap<String, Integer> ATMBills;
    private static final String outputFilePath = "phase2/src/resources/alerts.txt";

    static {
        /*
          To implement new denominations, edit the list below. No change of other code is needed.
         */
        List<Integer> denominations = Arrays.asList(5, 20, 10, 50);

        // Make sure it is in ascending order and immutable.
        Collections.sort(denominations);
        DENOMINATIONS = Collections.unmodifiableList(denominations);

        ATMBills = new HashMap<>();
        for (int d : DENOMINATIONS) {
            ATMBills.put(String.valueOf(d), 50);
        }
    }

    /**
     * Check the quantity of denominations
     *
     * @return true iff amount of any denomination goes below 20
     */
    private static boolean isAmountBelowTwenty() {
        for (int n : ATMBills.values()) {
            if (n < 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send an alert to alerts.txt iff isAmountBelowTwenty
     */
    private static void checkDenom() {
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
            out.println(ATMBills);
        }
    }

    static void cashDeposit(Map<Integer, Integer> deposits) {
        for (int d : deposits.keySet()) {
            ATMBills.put(String.valueOf(d), ATMBills.get(String.valueOf(d)) + deposits.get(d));
        }
    }

    /**
     * return the sorted mapping of denominators to the number of bills that will be withdrawn according to the
     * withdrawal amount and the inventory.
     */
    private static SortedMap<Integer, Integer> getDenominator(double amount) {
        double remainder = amount;
        SortedMap<Integer, Integer> returnBills = new TreeMap<>();

        for (int d = DENOMINATIONS.size() - 1; d >= 0; d--) {
            int denominatorWithdrawn = Math.min((int) Math.floor(remainder / DENOMINATIONS.get(d)), ATMBills.get(String.valueOf(DENOMINATIONS.get(d))));
            returnBills.put(DENOMINATIONS.get(d), denominatorWithdrawn);
            remainder -= denominatorWithdrawn * DENOMINATIONS.get(d);
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

            ATMBills.put(String.valueOf(denominator), ATMBills.get(String.valueOf(denominator)) - amountOfBills);
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
