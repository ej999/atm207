package ATM;

public class TradeOffer {
    private int quantity;
    private int price;
    private SystemUser_Customer  tradeUser;

    TradeOffer(int quantity, int price, SystemUser_Customer  tradeUser){
        this.quantity = quantity;
        this.price = price;
        this.tradeUser = tradeUser;
    }

    int getQuantity(){
        return this.quantity;
    }

    int getPrice(){
        return this.price;
    }

    SystemUser_Customer  getTradeUser(){
        return this.tradeUser;
    }
}
