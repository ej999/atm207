package ATM;

class TradeOffer {
    private final int quantity;
    private final int price;
    private final Customer tradeUser;

    TradeOffer(int quantity, int price, Customer tradeUser) {
        this.quantity = quantity;
        this.price = price;
        this.tradeUser = tradeUser;
    }

    @SuppressWarnings("WeakerAccess")
    public int getQuantity() {
        return this.quantity;
    }

    @SuppressWarnings("WeakerAccess")
    public int getPrice() {
        return this.price;
    }

    @SuppressWarnings("WeakerAccess")
    public Customer getTradeUser() {
        return this.tradeUser;
    }

    public String toString() {
        return "User:" + this.tradeUser.getUsername() + " Quantity:" + this.quantity +
                " Price:" + this.price;
    }
}
