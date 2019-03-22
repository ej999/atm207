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
 *
 * TODO:
 * - After successful trade, remove money from one person's account and add to other. DONE
 * - Check if a user has enough money before you can withdraw
 * - Add inventory functionality to users
 * - Cumulative offers
 * - Commission fee for bank
 * - Add forex functionality
 * - Add classes and sub-classes of items that can be traded (precious metals, jewels, foreign exchange)
 */

public class TradingSystem {
    HashMap<String, ArrayList<TradeOffer>> sell_offers = new HashMap<>();
    HashMap<String, ArrayList<TradeOffer>> buy_offers = new HashMap<>();
    TradingSystem() {
    }

    public void addSellOffer(String item, TradeOffer tradeoffer) {
        //If equal or better buy offer exists, make trade
        if(buy_offers.containsKey(item)){
            int quantity = tradeoffer.getQuantity();
            int price = tradeoffer.getPrice();
            SystemUser_Customer  user = tradeoffer.getTradeUser();
            ArrayList<TradeOffer> offers = buy_offers.get(item);
            for(int i = 0; i < offers.size(); i++){
                int other_quantity = offers.get(i).getQuantity();
                int other_price = offers.get(i).getPrice();
                SystemUser_Customer  other_user = offers.get(i).getTradeUser();
                //Checks if quantities are the same, price is better, and buyers has enough money
                if(other_quantity == quantity && other_price > price
                        && other_user.getPrimary().getBalance() > other_price){
                    user.getPrimary().deposit(other_price);
                    other_user.getPrimary().withdraw(other_price);
                    System.out.println("Offer made");
                    offers.remove(i);

                }
            }

        }
        //Else check for key existence, append trade offer to list.
        else {
            if(!sell_offers.containsKey(item)){
                sell_offers.put(item, new ArrayList<>());
            }
            sell_offers.get(item).add(tradeoffer);
        }

    }

    public void addBuyOffer(String item, TradeOffer tradeoffer) {
        //If equal or better sell offer exists, make trade
        if(sell_offers.containsKey(item)){
            int quantity = tradeoffer.getQuantity();
            int price = tradeoffer.getPrice();
            SystemUser_Customer  user = tradeoffer.getTradeUser();
            ArrayList<TradeOffer> offers = sell_offers.get(item);
            for(int i = 0; i < offers.size(); i++){
                int other_quantity = offers.get(i).getQuantity();
                int other_price = offers.get(i).getPrice();
                SystemUser_Customer  other_user = offers.get(i).getTradeUser();
                if(other_quantity == quantity && other_price < price
                        && user.getPrimary().getBalance() > other_price){
                    //Money will be exchanged.
                    user.getPrimary().withdraw(other_price);
                    other_user.getPrimary().deposit(other_price);
                    System.out.println("Offer made");
                    offers.remove(i);
                }
            }

        }
        //Else check for key existence, append trade offer to list.
        else {
            if(!buy_offers.containsKey(item)){
                buy_offers.put(item, new ArrayList<>());
            }
            buy_offers.get(item).add(tradeoffer);
        }

    }

}
