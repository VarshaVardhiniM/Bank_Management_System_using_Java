import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

//Account class representing a bank accound and its operations
public class Account implements Serializable { // Serializable for saving/loading account data
    private static final long serialVersionUID = 1L; // version control for Serializable

    private final String accountNumber; // Unique account number
    private final String holderName; // Name of account holder
    private final String pinHash; // pin
    private BigDecimal balance; // current account balance
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String holderName, String pinHash, BigDecimal initial) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pinHash = pinHash;
        this.balance = initial.setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(TransactionType.INITIAL, this.balance, this.balance, "Account opened"));
    }
    // Getter methods for account fields
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public String getPinHash() { return pinHash; }
    public BigDecimal getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }

    // Deposit money into account
    public void deposit(BigDecimal amount) {
        requirePositive(amount);
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount, balance, null));
    }

    // Withdraw money from account (if enough balance)
    public boolean withdraw(BigDecimal amount) {
        requirePositive(amount);
        if (balance.compareTo(amount) < 0) return false; // insufficient funds
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, balance, null));
        return true;
    }

    // internal helper for transfers (don’t record twice)
    void transferOut(BigDecimal amount, String toAcc) {
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(TransactionType.TRANSFER_OUT, amount, balance, "to " + toAcc));
    }

    void transferIn(BigDecimal amount, String fromAcc) {
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        transactions.add(new Transaction(TransactionType.TRANSFER_IN, amount, balance, "from " + fromAcc));
    }

    // Validation that an amount is positive and not null
    private void requirePositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
    }
}
