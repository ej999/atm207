package ATM;

import java.util.*;

import static ATM.ATM.accountManager;
import static ATM.ATM.userManager;

final class ETransferManager {
    //    private static HashMap<String, List<String>> contacts = new HashMap<>(); //maps customer to their added contacts
    List<ETransfer> allTransfers = new ArrayList<>();
    Map<String, HashMap<String, Double>> requests = new HashMap<>(); //keys: username of requester, item: responder, amount pair
    //maybe move this parameter to customer class?

    void send(String sender, String senderAccount, String recipient, String q, String a, double amount) {
        User re = userManager.getUser(recipient);
        Account recipientAccount = accountManager.getAccount(((Customer) re).getPrimaryAccount());
        assert recipientAccount != null;
        ETransfer transfer = new ETransfer(sender, senderAccount, recipientAccount.getId(), q, a, amount);
        allTransfers.add(transfer);
    }

//    void send(String sender, String senderAccount, List<String> recipient, String q, String a, double amount) {
//        // same as above except send same amount to multiple users
//        for (String r : recipient) {
//            send(sender, senderAccount, r, q, a, amount);
//        }
//    }

//    HashMap<String, Double> getAll(String recipient) {
//        //return all the transfers for this recipient that are not verified yet. keys: senders username, items: amount
//        //each key-value pair represents an ETransfer
//        HashMap<String, Double> transfers = new HashMap<>();
//        for (ETransfer e : allTransfers) {
//            if (!e.hasBeenDeposited() && e.getRecipient().equals(recipient)) {
//                transfers.put(e.getSender(), e.getAmount());
//            }
//        }
//        return transfers;
//    }

    ETransfer getOldestTransfer(String recipient) {
        // returns oldest unDeposited eTransfer to <recipient>
        ETransfer oldest = null;
        for (ETransfer e : allTransfers) {
            if (e.getRecipient().equals(recipient) && !e.hasBeenDeposited()) {
                oldest = e;
                break;
            }
        }
        return oldest;
    }

//    private List<ETransfer> getAllTransfers(String recipient) {
//        // returns all unDeposited eTransfer to <recipient>
//        List<ETransfer> transfers = new ArrayList<>();
//        for (ETransfer e : allTransfers) {
//            if (e.getRecipient().equals(recipient) && !e.hasBeenDeposited()) {
//                transfers.add(e);
//            }
//        }
//        return transfers;
//    }

    boolean validate(String response, Account account, String recipient) {
        // verifies most oldest unverified transfer and deposits the amount into recipient's account
        // returns true if successful
        ETransfer oldest = getOldestTransfer(recipient);

        if (oldest != null && oldest.verifyQuestion(response)) {
            if (((AccountTransferable) Objects.requireNonNull(accountManager.getAccount(oldest.getSenderAccount()))).transferToAnotherUser(oldest.getAmount(), recipient, account)) {
                oldest.deposit();
                return true;
            }  //else {oldest.unDeposit();}

        }
        return false;
    }

//    boolean validateAll(List<String> responses, Account account, String recipient) {
//    /*
//    verifies from most oldest to newest unverified transfers and deposits all amounts into recipient's account
//    deposits only when all of the responses are verified and correct
//    precondition: responses.size() == getAll.size()
//    */
//        List<ETransfer> transfers = getAllTransfers(recipient);
//
//        for (int i = 0; i < responses.size(); i++) {
//            boolean verified;
//            try {
//                verified = transfers.get(i).verifyQuestion(responses.get(i));
//            } catch (IndexOutOfBoundsException imLovingIt) {
//                imLovingIt.printStackTrace();
//                return false;
//            }
//            if (!verified) {
////                for (ETransfer e: transfers)
////                    e.unDeposit();
//                return false;
//            }
//        }
//        for (ETransfer e : transfers) {
//            boolean successful = ((AccountTransferable) Objects.requireNonNull(accountManager.getAccount(e.senderAccount))).transferToAnotherUser(e.getAmount(), recipient, account);
//            if (successful) {
//                e.deposit();
//            } else {
//                return false;
//            }
//        }
//        return true;
//    }

    void request(String requester, String responder, Double amount) {
        // record the fact that <requester> has requested <amount> from <responder>
        HashMap<String, Double> request = new HashMap<>();
        request.put(responder, amount);
        requests.put(requester, request);
    }

    HashMap<String, Double> readRequests(String responder) {
        // return all requests for <responder>
//        Iterator<Map.Entry<String, HashMap<String, Double>>> it = requests.entrySet().iterator();
//        while (it.hasNext()){
//            Map.Entry<String, HashMap<String, Double>> pair = it.next();
//        }
        HashMap<String, Double> ret = new HashMap<>();
        for (Map.Entry<String, HashMap<String, Double>> pair : requests.entrySet()) {
            String requester = pair.getKey();
            HashMap<String, Double> nameAndAmount = pair.getValue();
            if (nameAndAmount.containsKey(responder)) {
                ret.put(requester, nameAndAmount.get(responder));
            }
        }
        return ret;
    }

//    // For adding contacts and deleting requests
//    void updateContacts(String key, String value) {
//
//    }
}
