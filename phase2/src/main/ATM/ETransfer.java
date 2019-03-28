package ATM;

/** a single instance of an etransfer**/
public class ETransfer extends Transaction{
    private String question;
    private String answer;
    Customer sender;
    Account senderAccount;
    private boolean verified = false;
    private String recipient;


    public ETransfer(Customer sender, Account senderAccount, Account recipientAccount, String q, String a, double amount){
        super("ETransfer", amount, recipientAccount); //recipientAccount just for placedholder; receiver can
        //choose which account to deposit to
        this.senderAccount = senderAccount;
        this.question = q;
        this.answer = a;
        this.sender = sender;
        this.recipient = recipientAccount.getPrimaryOwner();
    }

    public boolean verifyQuestion(String input) {
       if(this.answer.equals(input)) {
           this.verified = true;
            return true;
       }
       return false;
    }

    public String getQuestion(){
        return this.question;
    }

    public boolean hasBeenDeposited(){
        return this.verified;
    }

    public String getRecipient(){
        return this.recipient;
    }

}
