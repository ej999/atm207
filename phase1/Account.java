package phase1;

abstract class Account {
    /*
    * There are two main types of accounts: Debt and Asset.
    */
    double accountBalance;
    Login_User accountOwner;
//    dateOfCreation;  figure out how to work with Time in Java

    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            accountBalance += depositAmount;
        }
    }

    /*
    The above deposit method is more like a helper method.
    The real deposit method reads individual lines from an input file called <deposits.txt>
     */

    abstract int withdraw(int withdrawalAmount);
    abstract String viewBalance();
}
