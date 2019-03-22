package phase2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the framework of the system that will be used for making trades. Basically, users will either
 * make an offer to buy something, like $10 for 5 gram of silver, or sell something, like $5 for 5 grams
 * of silver. In this case, since the buyer is offering equal to or more than the seller wants, the
 * trade will be made.
 * Trades are stored in the system as HashMaps of the item's name mapped to an ArrayList of TradingOffers,
 * and there are separate HashMaps for buying and selling.
 * When an offer is added, the system first checks if there exists a viable offer in the other HashMap, by
 * iterating through the ArrayList and first checking the quantities, and then the prices.
 * If no viable offers, it gets added to its HashMap.
 */

public class TradingSystem {
    HashMap<String, ArrayList<TradeOffer>> sell_offers = new HashMap<>();
    HashMap<String, ArrayList<TradeOffer>> buy_offers = new HashMap<>();

    TradingSystem() {
    }

    public void addSellOffer(String item, TradeOffer tradeoffer) {
        if (buy_offers.containsKey(item)) {
            int quantity = tradeoffer.getQuantity();
            int price = tradeoffer.getPrice();
            SystemUser user = tradeoffer.getTradeUser();
            ArrayList<TradeOffer> offers = buy_offers.get(item);
            for (int i = 0; i < offers.size(); i++) {
                if (offers.get(i).getQuantity() == quantity && offers.get(i).getPrice() > price) {
                    System.out.println("Offer made");
                }
            }

        }
        //sell_offers.put(item, tradeoffer);
    }

    public void addBuyOffer(String item, TradeOffer tradeoffer) {
        //buy_offers.put(item, tradeoffer);
    }
}
