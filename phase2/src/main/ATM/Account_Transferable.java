package ATM;

import java.io.IOException;

interface Account_Transferable {
    boolean payBill(double amount, String accountName) throws IOException;

    boolean transferBetweenAccounts(double transferAmount, Account account);

    boolean transferToAnotherUser(double transferAmount, User_Customer user, Account account);
}