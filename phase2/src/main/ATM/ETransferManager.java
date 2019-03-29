package ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final public class ETransferManager {
    static List<ETransfer> allTransfers = new ArrayList<>();
    static HashMap<String, HashMap<String, Double>> requests = new HashMap<>(); //keys: username of requester, item: requestee, amount pair

    static void send(Customer sender, Account senderAccount, String recipient, String q, String a, double amount){
        User re = ATM.userManager.getUser(recipient);
        Account recipientAccount = ATM.accountManager.getAccount(((Customer) re).getPrimary());
        ETransfer transfer = new ETransfer(sender, senderAccount, recipientAccount, q, a, amount);
        allTransfers.add(transfer);
    }

    //TODO: implement following
    static void send(Customer sender, Account senderAccount, List<String> recipient, String q, String a, double amount){
        // same as above except send same amount to multiple users
    }

    static HashMap<String, Double> getAll(String recipient){
        //return all the transfers for this recipient that are not verified yet. keys: sender usernames, items: amount

        return null;
    }

    static void validate(String response, Account account) {
        // verifies most oldest unverified transfers and deposits the amount into recipient's account
    }

    static void validateAll(List<String> responses, Account account){
        // verifies from most oldest to newest unverified transfers and deposits all amounts into recipient's account
    }

    static void request(String requester, String requestee, Double amount){
        // record the fact that <requester> has requested <amount> from <requestee>
    }

    static HashMap<String, Double> readRequests(String requestee){
        // return all requests for <requestee>
        return null;

    }

}
