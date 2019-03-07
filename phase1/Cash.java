package phase1;

/**
 * A class for handling cash storage, withdrawal, deposit of $5, $10, $20, and $50 bills.
 *
 *TODO
 * When the amount of any denomination goes below 20, your program should send an alert to a file called alerts.txt
 * that the real-life manager would read and handle by restocking the machine.
 *
 *TODO
 * When a user requests a withdrawal, your program will have to decide which bills to give to the user and decrease
 * the total of those denominations accordingly.
 */
class Cash {
    private int fiveDollarBill;
    private int tenDollarBill;
    private int twentyDollarBill;
    private int fiftyDollarBill;

    Cash(int fiveDollarBill, int tenDollarBill, int twentyDollarBill, int fiftyDollarBill) {
        this.fiveDollarBill = fiveDollarBill;
        this.tenDollarBill = tenDollarBill;
        this.twentyDollarBill = twentyDollarBill;
        this.fiftyDollarBill = fiftyDollarBill;
    }

    void cashDeposit(int five, int ten, int twenty, int fifty) {
        fiveDollarBill += five;
        tenDollarBill += ten;
        twentyDollarBill += twenty;
        fiveDollarBill += fifty;
    }

    boolean validCashWithdrawal(double amount) {
        double remainer = amount;

        if (fiftyDollarBill >= Math.floor(remainer / 50)){
            remainer = remainer % 50;
        }

        if (twentyDollarBill >= Math.floor(remainer / 20)){
            twentyDollarBill -= Math.floor(remainer / 10);
            remainer = remainer % 20;
        }

        if (tenDollarBill >= Math.floor(remainer / 10)){
            tenDollarBill -= Math.floor(remainer / 10);
            remainer = remainer % 10;
        }

        if (fiveDollarBill >= Math.floor(remainer / 5)){
            fiveDollarBill -= Math.floor(remainer / 5);
        }
        return true;
    }

    void cashWithdrawal(double amount) {
        double remainer = amount;

        if (fiftyDollarBill >= Math.floor(remainer / 50)){
            fiftyDollarBill -= Math.floor(remainer / 50);
            remainer = remainer % 50;
        }

        if (twentyDollarBill >= Math.floor(remainer / 20)){
            twentyDollarBill -= Math.floor(remainer / 10);
            remainer = remainer % 20;
        }

        if (tenDollarBill >= Math.floor(remainer / 10)){
            tenDollarBill -= Math.floor(remainer / 10);
            remainer = remainer % 10;
        }

        if (fiveDollarBill >= Math.floor(remainer / 5)){
            fiveDollarBill -= Math.floor(remainer / 5);
        }
    }

    public int getFiveDollarBill() {
        return fiveDollarBill;
    }

    public void setFiveDollarBill(int fiveDollarBill) {
        this.fiveDollarBill = fiveDollarBill;
    }

    public int getTenDollarBill() {
        return tenDollarBill;
    }

    public void setTenDollarBill(int tenDollarBill) {
        this.tenDollarBill = tenDollarBill;
    }

    public int getTwentyDollarBill() {
        return twentyDollarBill;
    }

    public void setTwentyDollarBill(int twentyDollarBill) {
        this.twentyDollarBill = twentyDollarBill;
    }

    public int getFiftyDollarBill() {
        return fiftyDollarBill;
    }

    public void setFiftyDollarBill(int fiftyDollarBill) {
        this.fiftyDollarBill = fiftyDollarBill;
    }
}
