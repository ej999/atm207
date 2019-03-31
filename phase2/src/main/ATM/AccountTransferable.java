package ATM;

import java.io.IOException;

interface AccountTransferable {
    boolean payBill(double amount, String accountName) throws IOException;

    boolean transferBetweenAccounts(double transferAmount, Account account);

    boolean transferToAnotherUser(double transferAmount, String username, Account account);
}