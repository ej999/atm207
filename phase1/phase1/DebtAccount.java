package phase1;

public abstract class DebtAccount implements Account {

    abstract double withdraw(double withdrawalAmount);

    abstract void deposit(double depositAmount);

    abstract String viewBalance();

}
