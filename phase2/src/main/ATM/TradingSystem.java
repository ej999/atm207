package ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static ATM.ATM.accountManager;

/**
 * This is the framework of the system that will be used for making trades. Basically, users will either
 * make an offer to buy something, like $10 for 5 integer unit of silver, or sell something, like $5 for 5 grams
 * of silver. In this case, since the buyer is offering equal to or more than the seller wants, the
 * trade will be made.
 * Trades are stored in the system as HashMaps of the item's name mapped to an ArrayList of TradingOffers,
 * and there are separate HashMaps for buying and selling.
 * When an offer is added, the system first checks if there exists a viable offer in the other HashMap, by
 * iterating through the ArrayList and first checking the quantities, and then the prices.
 * If no viable offers, it gets added to its HashMap.
 */

class TradingSystem {
    HashMap<String, ArrayList<TradeOffer>> sell_offers = new HashMap<>();
    HashMap<String, ArrayList<TradeOffer>> buy_offers = new HashMap<>();

    TradingSystem() {
    }

    void addSellOffer(String item, TradeOffer tradeoffer) {
        //If equal or better buy offer exists, make trade
        if (buy_offers.containsKey(item)) {
            if (makeNoTrade(item, tradeoffer, true)) {
                if (!sell_offers.containsKey(item)) {
                    sell_offers.put(item, new ArrayList<>());
                }

                sell_offers.get(item).add(tradeoffer);
            }

        }
        //Else check for if key exists, append trade offer to list.
        else {
            if (!sell_offers.containsKey(item)) {
                sell_offers.put(item, new ArrayList<>());
            }
            sell_offers.get(item).add(tradeoffer);
        }

    }

    void addBuyOffer(String item, TradeOffer tradeoffer) {
        //If equal or better sell offer exists, make trade
        if (sell_offers.containsKey(item)) {
            if (makeNoTrade(item, tradeoffer, false)) {
                if (!buy_offers.containsKey(item)) {
                    buy_offers.put(item, new ArrayList<>());
                }

                buy_offers.get(item).add(tradeoffer);
            }

        }
        //Else check for if key exists, append trade offer to list.
        else {
            if (!buy_offers.containsKey(item)) {
                buy_offers.put(item, new ArrayList<>());
            }
            buy_offers.get(item).add(tradeoffer);
        }

    }

    //Note: This method will remove all buy or sell offers of a user for a certain item
//    public void removeOffer(String item, Customer user, boolean selling) {
//        if (selling) {
//            ArrayList<TradeOffer> offers = sell_offers.get(item);
//            for (int i = 0; i < offers.size(); i++) {
//                if (offers.get(i).getTradeUser() == user) {
//                    offers.remove(i);
//                    i--;
//                    System.out.println("Sell offer removed");
//                }
//            }
//        } else {
//            ArrayList<TradeOffer> offers = buy_offers.get(item);
//            for (int i = 0; i < offers.size(); i++) {
//                if (offers.get(i).getTradeUser() == user) {
//                    offers.remove(i);
//                    i--;
//                    System.out.println("Buy offer removed");
//                }
//            }
//        }
//    }


    private boolean makeNoTrade(String item, TradeOffer tradeoffer, boolean selling) {

        HashMap<String, ArrayList<TradeOffer>> offers_map;
        //default values, but these will never get used
        Customer seller = tradeoffer.getTradeUser();
        Customer buyer = tradeoffer.getTradeUser();
        int sell_price = 0;
        int buy_price = 0;

        if (selling) {
            offers_map = buy_offers; //Because if we're selling, we're look for buy offers.
            sell_price = tradeoffer.getPrice();
            seller = tradeoffer.getTradeUser();
        } else {
            offers_map = sell_offers;
            buy_price = tradeoffer.getPrice();
            buyer = tradeoffer.getTradeUser();
        }

        int quantity = tradeoffer.getQuantity();
        ArrayList<TradeOffer> offers = offers_map.get(item);

        for (int i = 0; i < offers.size(); i++) {
            if (selling) {
                buy_price = offers.get(i).getPrice();
                buyer = offers.get(i).getTradeUser();
            } else {
                sell_price = offers.get(i).getPrice();
                seller = offers.get(i).getTradeUser();
            }
            int other_quantity = offers.get(i).getQuantity();

            if (other_quantity == quantity && buy_price >= sell_price
                    && Objects.requireNonNull(accountManager.getAccount(buyer.getPrimaryAccount())).getBalance() >= buy_price
                    && seller.getInventory().itemAmount(item) >= quantity

            ) {
                Objects.requireNonNull(accountManager.getAccount(seller.getPrimaryAccount())).deposit(buy_price);
                Objects.requireNonNull(accountManager.getAccount(buyer.getPrimaryAccount())).withdraw(buy_price);
                buyer.getInventory().depositItem(item, quantity);
                seller.getInventory().withdrawItem(item, quantity);
                System.out.println("Offer made");
                offers.remove(i);
                return false;
            }
        }
        return true;

    }


    ArrayList<String> seeOffers(String item, boolean selling) {

        HashMap<String, ArrayList<TradeOffer>> offers_map;
        ArrayList<String> returned = new ArrayList<>();
        if (selling) {
            offers_map = sell_offers;
        } else {
            offers_map = buy_offers;
        }
        if (offers_map.containsKey(item)) {
            ArrayList<TradeOffer> offers = offers_map.get(item);
            for (TradeOffer offer : offers) {
                returned.add(offer.toString());
            }

        }
        return returned;
    }


}
