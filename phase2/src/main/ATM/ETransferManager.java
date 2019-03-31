package ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ETransferManager {
    List<ETransfer> allTransfers = new ArrayList<>();
    Map<String, HashMap<String, Double>> requests = new HashMap<>(); //keys: username of requester, item: requestee, amount pair
    private static HashMap<String, List<String>> contancts = new HashMap<>(); //maps customer to their added contacts
    //maybe move this parameter to customer class?

    void send(Customer sender, AccountTransferable senderAccount, String recipient, String q, String a, double amount){
        User re = ATM.userManager.getUser(recipient);
        Account recipientAccount = ATM.accountManager.getAccount(((Customer) re).getPrimaryAccount());
        ETransfer transfer = new ETransfer(sender, senderAccount, recipientAccount, q, a, amount);
        allTransfers.add(transfer);
    }

    void send(Customer sender, AccountTransferable senderAccount, List<String> recipient, String q, String a, double amount){
        // same as above except send same amount to multiple users
        for (String r: recipient) {
            send(sender, senderAccount, r, q, a, amount);
        }
    }

    HashMap<String, Double> getAll(String recipient) {
        //return all the transfers for this recipient that are not verified yet. keys: sender usernames, items: amount
        //each key-value pair represents an ETransfer
        HashMap<String, Double> transfers = new HashMap<>();
        for (ETransfer e : allTransfers) {
            if (!e.hasBeenDeposited() && e.getRecipient().equals(recipient)) {
                transfers.put(e.sender.getUsername(), e.getAmount());
            }
        }
        return transfers;

    }

    ETransfer getOldestTransfer(String recipient){
        // returns oldest undeposited etransfer to <recipient>
        ETransfer oldest = null;
        for (ETransfer e: allTransfers) {
            if (e.getRecipient().getUsername().equals(recipient) && !e.hasBeenDeposited()) {
                oldest = e;
                break;
            }
        }
        return oldest;
    }

    List<ETransfer> getAllTransfers(String recipient){
        // returns all undeposited etransfer to <recipient>
        List<ETransfer> transfers = new ArrayList<>();
        for (ETransfer e: allTransfers){
            if (e.getRecipient().getUsername().equals(recipient) && !e.hasBeenDeposited()){
                transfers.add(e);
            }
        }
        return transfers;
    }

    boolean validate(String response, Account account, String recipient) {
        // verifies most oldest unverified transfer and deposits the amount into recipient's account
        // returns true if successful
        ETransfer oldest = getOldestTransfer(recipient);

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

    boolean validateAll(List<String> responses, Account account, String recipient){
        // verifies from most oldest to newest unverified transfers and deposits all amounts into recipient's account
        // deposits only when all of the responses are verified and correct
        // precondition: responses.size() == getAll.size()
        List<ETransfer> transfers = getAllTransfers(recipient);

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

    void request(String requester, String requestee, Double amount){
        // record the fact that <requester> has requested <amount> from <requestee>
        HashMap<String, Double> request = new HashMap<>();
        request.put(requestee, amount);
        requests.put(requester, request);
    }

    HashMap<String, Double> readRequests(String requestee){
        // return all requests for <requestee>
//        Iterator<Map.Entry<String, HashMap<String, Double>>> it = requests.entrySet().iterator();
//        while (it.hasNext()){
//            Map.Entry<String, HashMap<String, Double>> pair = it.next();
//        }
        HashMap<String, Double> ret = new HashMap<>();
        for (Map.Entry<String, HashMap<String, Double>> pair: requests.entrySet()){
            String requester = pair.getKey();
            HashMap<String, Double> nameAndAmount = pair.getValue();
            if (nameAndAmount.containsKey(requestee)){
                ret.put(requester, nameAndAmount.get(requestee));
            }
        }
        return ret;
    }
    //TODO: implement methods for adding contacts and deleting requests
    void updateContacts(String key, String value){

    }
}
