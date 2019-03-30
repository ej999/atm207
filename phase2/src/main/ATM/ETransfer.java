package ATM;

/**
 * a single instance of an etransfer
 **/
public class ETransfer extends Transaction {
    Customer sender;
    AccountTransferable senderAccount;
    private String question;
    private String answer;
    private boolean deposited = false;
    private Customer recipient;


    public ETransfer(Customer sender, AccountTransferable senderAccount, Account recipientAccount, String q, String a, double amount) {
        super("ETransfer", amount, recipientAccount, senderAccount.getClass().getName()); //recipientAccount just for placedholder; receiver can
        //choose which account to deposit to
        this.senderAccount = senderAccount;
        this.question = q;
        this.answer = a;
        this.sender = sender;
        this.recipient = recipientAccount.getPrimaryOwner();
    }

    public boolean verifyQuestion(String input) {
        if (this.answer.equals(input)) {
            return true;
        }
        return false;
    }

    public String getQuestion() {
        return this.question;
    }

    public boolean hasBeenDeposited() {
        return this.deposited;
    }

    public Customer getRecipient() {
        return this.recipient;
    }

    public void deposit(){
        this.deposited = true;
    }
    public void undeposit(){
        this.deposited = false;
    }

}
