package ATM;

import javafx.scene.Scene;
import javafx.stage.Stage;

class Teller extends UserEmployee {

    private static final String type = Teller.class.getName();

    public Teller(String username, String password) {
        super(username, password);

    }

    @SuppressWarnings("unused")
    public String getType() {
        return type;
    }

    @Override
    Scene createOptionsScreen(Stage window, Scene welcomeScreen) {
        EmployeeOptionsGUI gui = new EmployeeOptionsGUI(window, welcomeScreen, this);
        return gui.createOptionsScreen();
    }

}
