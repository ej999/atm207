package ATM;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

import static ATM.ATM.banknoteManager;

class BankManager extends UserEmployee {

    private static final String type = BankManager.class.getName();

    public BankManager(String username, String password) {
        super(username, password);
    }

    @SuppressWarnings("unused")
    public String getType() {
        return type;
    }

    void restockMachine(Map<Integer, Integer> deposits) {
        banknoteManager.banknoteDeposit(deposits);
    }

    @Override
    Scene createOptionsScreen(Stage window, Scene welcomeScreen) {
        BankManagerOptionsGUI gui = new BankManagerOptionsGUI(window, welcomeScreen, this);
        return gui.createOptionsScreen();
    }
}
