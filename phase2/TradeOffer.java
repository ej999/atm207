package phase2;

public class TradeOffer {
    int quantity;
    int price;
    SystemUser_Customer  tradeUser;

    TradeOffer(int quantity, int price, SystemUser_Customer  tradeUser){
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

    public SystemUser_Customer  getTradeUser(){
        return this.tradeUser;
    }
}
