package ATM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


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

    void restockMachine(Map<Integer, Integer> deposits) {
        new Cash().cashDeposit(deposits);
    }

//    /**
//     * The Bank Manager has the ability to undo the most recent transaction on any asset or debt account,
//     * except for paying bill.
//     *
//     * @param account account involved
//     */
//    void undoMostRecentTransaction(Account account) {
//        account.undoMostRecentTransaction();
//    }

    /**
     * Undo the n most recent transactions
     *
     * @param account bank account
     * @param n       number of transactions
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
}
