package phase1;

import java.util.HashMap;

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
    HashMap<String, Integer> bills;

    Cash(int fiveDollarBill, int tenDollarBill, int twentyDollarBill, int fiftyDollarBill) {
        bills.put("five", fiveDollarBill);
        bills.put("ten", tenDollarBill);
        bills.put("twenty", twentyDollarBill);
        bills.put("fifty", fiftyDollarBill);
    }

    void cashDeposit(int five, int ten, int twenty, int fifty) {
        bills.put("five", bills.get("five") + five);
        bills.put("ten", bills.get("ten") + ten);
        bills.put("twenty", bills.get("twenty") + twenty);
        bills.put("fifty", bills.get("fifty") + fifty);
    }

    boolean validCashWithdrawal(double amount) {
        double remainer = amount;
        //TODO reimpement. the following is wrong.
        fifty
        if (bills.get("fifty") >= Math.floor(remainer / 50)){
            bills.put("fifty", bills.get("fifty") - Math.floor(remainer / 50));
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
