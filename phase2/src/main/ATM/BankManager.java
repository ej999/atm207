package ATM;

import javafx.scene.Scene;
import javafx.stage.Stage;

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
        ATM.banknoteManager.banknoteDeposit(deposits);
    }

    @Override
    Scene createOptionsScreen(Stage window, Scene welcomeScreen) {
        BankManagerOptionsGUI gui = new BankManagerOptionsGUI(window, welcomeScreen, this);
        return gui.createOptionsScreen();
    }

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
