package ATM;

import java.io.Serializable;

/**
 * A instance of a ETransfer
 **/
public class ETransfer extends Transaction implements Serializable {
    Customer sender;
    AccountTransferable senderAccount;
    private String question;
    private String answer;
    private boolean deposited = false;
    private String recipient;


    public ETransfer(Customer sender, AccountTransferable senderAccount, Account recipientAccount, String q, String a, double amount) {
        //recipientAccount just for placeholder; receiver can choose which account to deposit to
        super("ETransfer", amount, recipientAccount, senderAccount.getClass().getName());
        this.senderAccount = senderAccount;
        this.question = q;
        this.answer = a;
        this.sender = sender;
        this.recipient = recipientAccount.getPrimaryOwner();
    }

    public boolean verifyQuestion(String input) {
        return this.answer.equals(input);
    }

    public String getQuestion() {
        return this.question;
    }

    public boolean hasBeenDeposited() {
        return this.deposited;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void deposit() {
        this.deposited = true;
    }

    public void unDeposit() {
        this.deposited = false;
    }

}
