public abstract class DebtAccount implements Account {
    abstract void withdrawal(int withdrawalAmount);

    abstract void deposit(double depositAmount);

    abstract String viewBalance();

}
