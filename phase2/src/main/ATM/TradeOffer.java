package ATM;

public class TradeOffer {
    private int quantity;
    private int price;
    private Customer tradeUser;

    TradeOffer(int quantity, int price, Customer tradeUser) {
        this.quantity = quantity;
        this.price = price;
        this.tradeUser = tradeUser;
    }

    int getQuantity() {
        return this.quantity;
    }

    int getPrice() {
        return this.price;
    }

    Customer getTradeUser() {
        return this.tradeUser;
    }

    public String toString() {
        return "User:" + this.tradeUser.getUsername() + " Quantity:" + this.quantity +
                " Price:" + this.price;
    }
}
