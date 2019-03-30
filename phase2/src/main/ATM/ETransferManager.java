package ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final public class ETransferManager {
    private static List<ETransfer> allTransfers = new ArrayList<>();
    private static HashMap<String, HashMap<String, Double>> requests = new HashMap<>(); //keys: username of requester, item: requestee, amount pair

    static void send(Customer sender, AccountTransferable senderAccount, String recipient, String q, String a, double amount){
        User re = ATM.userManager.getUser(recipient);
        Account recipientAccount = ATM.accountManager.getAccount(((Customer) re).getPrimaryAccount());
        ETransfer transfer = new ETransfer(sender, senderAccount, recipientAccount, q, a, amount);
        allTransfers.add(transfer);
    }

    //TODO: implement following
    static void send(Customer sender, AccountTransferable senderAccount, List<String> recipient, String q, String a, double amount){
        // same as above except send same amount to multiple users
        for (String r: recipient) {
            send(sender, senderAccount, r, q, a, amount);
        }
    }

    static HashMap<String, Double> getAll(String recipient){
        //return all the transfers for this recipient that are not verified yet. keys: sender usernames, items: amount
        //each key-value pair represents an ETransfer
        HashMap<String, Double> transfers = new HashMap<>();
        for (ETransfer e: allTransfers){
            if (!e.hasBeenDeposited() && e.getRecipient().equals(recipient)){
                transfers.put(e.sender.getUsername(), e.getAmount());
            }
        }
        return transfers;

    }

    static boolean validate(String response, Account account, String recipient) {
        // verifies most oldest unverified transfer and deposits the amount into recipient's account
        // returns true if successful
        ETransfer oldest = null;
        for (ETransfer e: allTransfers){
            if (e.getRecipient().equals(recipient) && !e.hasBeenDeposited()){
                oldest = e;
                break;
            }
        }
        if (oldest != null && oldest.verifyQuestion(response)){
            if (oldest.senderAccount.transferToAnotherUser(oldest.getAmount(), (Customer)ATM.userManager.getUser(recipient), account)){
                oldest.deposit();
                return true;
            }
            else{
                //oldest.undeposit();
            }
        }
        return false;
    }

    static boolean validateAll(List<String> responses, Account account, String recipient){
        // verifies from most oldest to newest unverified transfers and deposits all amounts into recipient's account
        // deposits only when all of the responses are verified and correct
        // precondition: responses.size() == getAll.size()
        List<ETransfer> transfers = new ArrayList<>();
        for (ETransfer e: allTransfers){
            if (e.getRecipient().equals(recipient)){
                transfers.add(e);
            }
        }
        for (int i = 0; i < responses.size(); i++){
            boolean verified;
            try{
                verified = transfers.get(i).verifyQuestion(responses.get(i));
            }
            catch(IndexOutOfBoundsException iloveit){
                iloveit.printStackTrace();
                return false;
            }
            if (!verified){
//                for (ETransfer e: transfers)
//                    e.undeposit();
                return false;
            }
        }
        for (ETransfer e: transfers){
            boolean successful = e.senderAccount.transferToAnotherUser(e.getAmount(), (Customer)ATM.userManager.getUser(recipient), account);
            if (successful){
                e.deposit();
            }
            else{
                return false;
            }
        }
        return true;
    }

    static void request(String requester, String requestee, Double amount){
        // record the fact that <requester> has requested <amount> from <requestee>
    }

    static HashMap<String, Double> readRequests(String requestee){
        // return all requests for <requestee>
        return null;

    }

}
