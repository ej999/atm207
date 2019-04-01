package ATM;

import java.io.Serializable;
import java.util.Objects;

import static ATM.ATM.accountManager;

/**
 * A instance of a ETransfer
 **/
class ETransfer extends Transaction implements Serializable {
    private final String type = this.getClass().getName();
    private final String senderAccount;
    private final String question;
    private final String response;
    private final String recipient;
    private String senderUsername;
    private boolean deposited;

    @SuppressWarnings("WeakerAccess")
    public ETransfer(String senderUsername, String senderAccount, String recipientAccountId, String q, String a, double amount) {
        //recipientAccount just for placeholder; receiver can choose which account to deposit to
        super("ETransfer", amount, recipientAccountId, senderAccount.getClass().getName());
        this.senderAccount = senderAccount;
        this.question = q;
        this.response = a;
        this.senderUsername = senderUsername;
        this.recipient = Objects.requireNonNull(accountManager.getAccount(recipientAccountId)).getPrimaryOwner();
        this.deposited = false;
    }

    @SuppressWarnings("unused")
    public boolean isDeposited() {
        return deposited;
    }

    @SuppressWarnings("WeakerAccess, unused")
    public String getSenderAccount() {
        return senderAccount;
    }

    boolean verifyQuestion(String input) {
        return this.response.equals(input);
    }

    @SuppressWarnings("WeakerAccess")
    public String getQuestion() {
        return this.question;
    }

    @SuppressWarnings("unused")
    public String getResponse() {
        return this.response;
    }

    boolean hasBeenDeposited() {
        return this.deposited;
    }

    @SuppressWarnings("WeakerAccess")
    public String getRecipient() {
        return this.recipient;
    }

    public void deposit() {
        this.deposited = true;
    }

//    public void unDeposit() {
//        this.deposited = false;
//    }

    @SuppressWarnings("unused")
    public String getSenderUsername() {
        return this.senderUsername;
    }

    @SuppressWarnings("unused")
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    @Override
    public String toString() {
        return "eTransfer of $" + getAmount() + " from " + senderUsername + " on " + getDateCreatedReadable();
    }

    @SuppressWarnings("unused")
    public String getType() {
        return type;
    }
}
