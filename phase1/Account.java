package phase1;

interface Account {
    /*
    * There are two main types of accounts: Debt and Asset.
    */

    void deposit(double depositAmount);
    double withdraw(double withdrawalAmount);
    String viewBalance();
}
