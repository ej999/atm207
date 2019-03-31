package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * A utility class that handles storage, withdrawal, deposit of banknotes.
 */
final class BanknoteManager {
    private static final String OUTPUT_FILE_PATH = "phase2/src/resources/alerts.txt";
    final List<Integer> DENOMINATIONS;

    // A mapping of denomination to quantity
    HashMap<String, Integer> banknotes;

    BanknoteManager() {
        // To implement new denominations, edit the list below. No change of other code is needed.
        List<Integer> denominations = Arrays.asList(5, 20, 10, 50);

        // Make sure it is in ascending order and immutable.
        Collections.sort(denominations);
        DENOMINATIONS = Collections.unmodifiableList(denominations);
    }

    /**
     * Check the quantity of denominations
     *
     * @return true iff amount of any denomination goes below 20
     */
    private boolean isAmountBelowTwenty() {
        for (int n : banknotes.values()) {
            if (n < 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send an alert to alerts.txt iff isAmountBelowTwenty
     */
    private void checkDenominator() {
        if (isAmountBelowTwenty()) {
            try {
                sendAlert("Banknote stock is running low: " + banknotes);
            } catch (IOException e) {
                System.err.println("Failed to send alert notification about insufficient banknote stock");
            }
        }
    }

    private void sendAlert(String message) throws IOException {
        // Open the file for writing and write to it.
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH, true)))) {
            out.println(message);
        }
    }

    /**
     * return the sorted mapping of denominators to the number of bills that will be withdrawn according to the
     * withdrawal amount and the inventory.
     */
    private SortedMap<Integer, Integer> getDenominator(double amount) {
        double remainder = amount;
        SortedMap<Integer, Integer> banknotes = new TreeMap<>();

        for (int d = DENOMINATIONS.size() - 1; d >= 0; d--) {
            Integer denominator = DENOMINATIONS.get(d);

            String b = String.valueOf(this.banknotes.get(String.valueOf(denominator)));

            int denominatorWithdrawn = Math.min((int) Math.floor(remainder / denominator), Integer.valueOf(b));
            banknotes.put(DENOMINATIONS.get(d), denominatorWithdrawn);
            remainder -= denominatorWithdrawn * DENOMINATIONS.get(d);
        }

        return banknotes;
    }

    /**
     * In order for a withdrawal to take place, there must be enough bills to give out.
     *
     * @param amount withdrawal amount
     * @return true iff there is enough bills for amount
     */
    boolean isThereEnoughBankNote(double amount) {
        Map<Integer, Integer> numberOfBankNote = getDenominator(amount);

        double total = 0;

        for (int denominator : numberOfBankNote.keySet()) {
            total += denominator * numberOfBankNote.get(denominator);
        }

        return amount == total;
    }

    // The number of different bills are used in withdrawal depending on the withdrawal amount and the inventory.
    void banknoteWithdrawal(double amount) {
        Map<Integer, Integer> numOfBanknotes = getDenominator(amount);

        StringBuilder print = new StringBuilder();
        int total = 0;

        for (int denominator : numOfBanknotes.keySet()) {
            int numOfBanknote = numOfBanknotes.get(denominator);

            String n = String.valueOf(banknotes.get(String.valueOf(denominator)));

            banknotes.put(String.valueOf(denominator), Integer.valueOf(n) - numOfBanknote);
            total += denominator * numOfBanknote;

            print.append(numOfBanknote).append(" of $").append(denominator).append("-bill, ");
        }
        checkDenominator();
        System.out.println(print + "in total of " + total + " have been withdrawn");
    }

    void banknoteDeposit(Map<Integer, Integer> deposits) {
        for (Integer d : deposits.keySet()) {

            String s = String.valueOf(banknotes.get(String.valueOf(d)));
            Integer currentBanknote = Integer.valueOf(s);

            banknotes.put(d.toString(), currentBanknote + deposits.get(d));
        }
    }

}
