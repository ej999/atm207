package ATM;

public class SystemUser_Employee_Teller extends SystemUser_Employee {

    private static final String user_type = SystemUser_Employee_Teller.class.getName();

    SystemUser_Employee_Teller(String username, String password) {
        super(username, password);

    }

    public String getUser_type() {
        return user_type;
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
//    void addAccount(String accountType, SystemUser_Customer username) {
//        this.addAccount(accountType, username, 0);
//    }
//
//    /**
//     * Allow BankManger to read alerts.
//     */
//    void readAlerts() {
//        BufferedReader reader;
//        try {
//            reader = new BufferedReader(new FileReader("phase1/alerts.txt"));
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
