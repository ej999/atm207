package ATM;

public class Teller extends UserEmployee {

    private static final String type = Teller.class.getName();

    public Teller(String username, String password) {
        super(username, password);

    }

    public String getType() {
        return type;
    }

//    void undoMostRecentTransaction(Account account) {
//        account.undoMostRecentTransaction();
//    }
//
//    /**
//     * Create an account for a Customer. Amount is not initialized here.
//     */
//    void addAccount(String accountType, Customer username) {
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
