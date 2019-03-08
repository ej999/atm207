package phase1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A customer's login account, with username, password, list of their accounts, primary account, and net total.
 */
class Login_Customer extends Login {
    private final ArrayList<Account> accounts;
    private Account primary;

    Login_Customer(String username, String password){
        super(username, password, "Customer");
        this.accounts = new ArrayList<>();
    }

    void addAccount(Account account){
        this.accounts.add(account);
        // If a user has only one checking account, it will be the default destination for any deposits.
        if(primary == null && account instanceof Account_Asset_Chequing){
            primary = account;
        }
    }

    void setPrimary(Account primary) {
        this.primary = primary;
    }

    ArrayList<Account> getAccounts() {
        return accounts;
    }

    boolean hasAccount(Account account){
        for(Account a : this.accounts){
            if(a.equals(account)){
                return true;
            }

        }
        return false;
    }

    // The total of their debt account balances subtracted from the total of their asset account balances.
    double netTotal() {
        double sum = 0;
        for (Account a : this.accounts) {
            if (a instanceof Account_Asset) {
                sum += a.getBalance();
            } else {
                sum -= a.getBalance();
            }
        }
        return sum;
    }

    void displayOptions(){
        System.out.println("1. Show summary of all account balances");
        System.out.println("2. View an account.");
        System.out.println("3. See net worth.");
    }

    void selectOption(int o){
        switch(o) {
            case 1:
                StringBuilder returnMessage = new StringBuilder();
                for(Account account:accounts){
                    System.out.println(account.getClass() + ": " + account.getBalance());
            }
                break;
            case 2:
                System.out.println("Select the account you would like to work with:");
                    HashMap<Integer, Account> option = new HashMap<>();
                    int i = 1;
                    for(Account account: accounts){
                        System.out.println(i + ". " + account.toString());
                        option.put(i, account);
                        i += 1;
                    }
                Scanner reader = new Scanner(System.in);
                int accountNumber = reader.nextInt();
                selectAccount(option.get(accountNumber));
                break;


            case 4:
                System.out.println(netTotal());
                break;
        }
    }

    void selectAccount(Account account){
        System.out.println("1. Show account creation date.");
        System.out.println("2. Show account balance.");
        System.out.println("3. Show most recent transaction");
        Scanner reader = new Scanner(System.in);
        int choice = reader.nextInt();
        switch(choice){
            case 1:
                System.out.println(account.dateOfCreation);
                break;
            case 2:
                System.out.println(account.getBalance());
                break;
            case 3:
                System.out.println("Type :" + account.recentTransaction.get("Type"));
                System.out.println("Amount :" + account.recentTransaction.get("Amount"));
                break;

        }




    }
}
