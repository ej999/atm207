package ATM;

import java.util.List;

/**
 * A chequing account.
 */
class Chequing extends AccountAsset {
    private static final String type = Chequing.class.getName();

    @SuppressWarnings("unused")
    public Chequing(String id, List<String> owners) {
        super(id, owners);
    }

    @SuppressWarnings("unused")
    public Chequing(String id, String owner) {
        super(id, owner);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    void withdraw(double withdrawalAmount) {
        super.withdraw(withdrawalAmount, getBalance() > 0 & (getBalance() - withdrawalAmount >= -100));
    }
}
