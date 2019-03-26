package ATM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


//import java.util.Account;
//Import ATM


class BankManager extends UserEmployee {

    private static final String type = BankManager.class.getName();

    public BankManager(String username, String password) {
        super(username, password);
    }

    public String getType() {
        return type;
    }

    /**
     * Bank Manager has the ability to restock cash machine.
     *
     * @param cashList amount of denominations [fives, tens, twenties, fifties]
     */
    void restockMachine(ArrayList<Integer> cashList) {
        Cash.cashDeposit(cashList);
    }

//    /**
//     * The Bank Manager has the ability to undo the most recent transaction on any asset or debt account,
//     * except for paying bills.
//     *
//     * @param account account involved
//     */
//    void undoMostRecentTransaction(Account account) {
//        account.undoMostRecentTransaction();
//    }

    /**
     * Undo the n most recent transactions
     * @param account bank account
     * @param n number of transactions
     */
    void undoTransactions(Account account, int n) {
        account.undoTransactions(n);
    }

    void setMaxTransactions(Youth account, int transactionsAmount) {
        account.setMaxTransactions(transactionsAmount);
    }

    void setTransferLimit(Youth account, int transferLimitAmount) {
        account.setTransferLimit(transferLimitAmount);
    }

    void readAlerts() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("phase2/src/resources/alerts.txt"));
            String alert = reader.readLine();
            while (alert != null) {
                System.out.println(alert);
                alert = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Bank Manager with username \"" + getUsername() + "\" and password \"" + getPassword() + "\"";
    }

}
