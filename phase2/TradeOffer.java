package phase2;

public class TradeOffer {
    int quantity;
    int price;
    SystemUser tradeUser;

    TradeOffer(int quantity, int price, SystemUser tradeUser) {
        this.quantity = quantity;
        this.price = price;
        this.tradeUser = tradeUser;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public SystemUser getTradeUser() {
        return this.tradeUser;
    }
}
