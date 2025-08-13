import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Enum for transaction categories
enum TransactionType { INITIAL, DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT }

// Representing a single account transaction
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalDateTime timestamp; // When the transaction happened
    private final TransactionType type; // Kind of transaction
    private final BigDecimal amount; // transaction account
    private final BigDecimal balanceAfter; // Balance after it was applied
    private final String note; // Optional description

    public Transaction(TransactionType type, BigDecimal amount, BigDecimal balanceAfter, String note) {
        this.timestamp = LocalDateTime.now(); // Set to current time
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.note = note;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getNote() { return note; }

    // Format transaction as a CSV row, escaping quotes in notes
    public String toCsvRow() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.join(",",
            fmt.format(timestamp),
            type.name(),
            amount.toPlainString(),
            balanceAfter.toPlainString(),
            "\"" + (note == null ? "" : note.replace("\"", "\"\"")) + "\""
        );
    }

    // Nicely formatted string for console or GUI history display
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fmt.format(timestamp) + " | " + type + " | " + amount + " | Bal: " + balanceAfter +
               (note == null || note.isEmpty() ? "" : " | " + note);
    }
}
