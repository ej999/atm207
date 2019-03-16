package phase1;

public class TradeOffer {
    int quantity;
    int price;
    Login tradeUser;

    TradeOffer(int quantity, int price, Login tradeUser){
        this.quantity = quantity;
        this.price = price;
        this.tradeUser = tradeUser;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public int getPrice(){
        return this.price;
    }

    public Login getTradeUser(){
        return this.tradeUser;
    }
}
