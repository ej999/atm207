package ATM;

public class User_Employee_Teller extends User_Employee {

    private static final String type = User_Employee_Teller.class.getName();

    public User_Employee_Teller(String username, String password) {
        super(username, password);

    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Teller with username \"" + getUsername() + "\" and password \"" + getPassword() + "\"";
    }

//    void undoMostRecentTransaction(Account account) {
//        account.undoMostRecentTransaction();
//    }
//
//    /**
//     * Create an account for a Customer. Amount is not initialized here.
//     */
//    void addAccount(String accountType, User_Customer username) {
//        this.addAccount(accountType, username, 0);
//    }
//
//    /**
//     * Allow BankManger to read alerts.
//     */
//    void readAlerts() {
//        BufferedReader reader;
//        try {
//            reader = new BufferedReader(new FileReader("phase2/src/resources/alerts.txt"));
//            String alert = reader.readLine();
//            while (alert != null) {
//                System.out.println(alert);
//                alert = reader.readLine();
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
