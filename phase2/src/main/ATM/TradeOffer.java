package ATM;

public class TradeOffer {
    private int quantity;
    private int price;
    private User_Customer tradeUser;

    TradeOffer(int quantity, int price, User_Customer tradeUser) {
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

    User_Customer getTradeUser() {
        return this.tradeUser;
    }
}
