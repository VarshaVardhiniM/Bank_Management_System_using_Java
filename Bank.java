import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.util.*;

// Bank class manages multiple account objects and banking operations
public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Account> accounts = new HashMap<>();
    private long nextAccount = 100100L; // for generating new account numbers

    // ---------- Persistence ----------
    // loads bank data from file; otherwise create new bank
    public static Bank load(String file) {
        File f = new File(file);
        if (!f.exists()) return new Bank();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            if (o instanceof Bank) return (Bank) o;
        } catch (Exception ignored) {}
        return new Bank();
    }

    // save bank data to file
    public boolean save(String file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ---------- Utilities ----------
    // utilities: Hash a string using SHA-256
    public static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(s.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b)); // converts byte -> hex
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    // Generate a new account number (increments internally)
    private String newAccountNumber() { return "ACC" + (nextAccount++); }

    // ---------- Core operations ----------
    // Create a new account (name required, PIN 4-6 digits, optional deposit)
    public synchronized Account createAccount(String holderName, String pin, BigDecimal initialDeposit) {
        if (holderName == null || holderName.trim().isEmpty()) throw new IllegalArgumentException("Name required");
        if (pin == null || !pin.matches("\\d{4,6}")) throw new IllegalArgumentException("PIN must be 4-6 digits");
        BigDecimal init = initialDeposit == null ? BigDecimal.ZERO : initialDeposit.setScale(2, RoundingMode.HALF_UP);
        String accNo = newAccountNumber();
        Account acc = new Account(accNo, holderName.trim(), sha256(pin), init); // stores hashed pin
        accounts.put(accNo, acc);
        return acc;
    }

    // Get account by account number
    public Account getAccount(String accNo) { return accounts.get(accNo); }

    // Authenticate account using account number and PIN
    public Account authenticate(String accNo, String pin) {
        Account a = accounts.get(accNo);
        if (a == null) return null;
        return sha256(pin).equals(a.getPinHash()) ? a : null;
    }

    // Deposit money to account
    public synchronized void deposit(String accNo, BigDecimal amount) {
        Account a = getRequired(accNo);
        a.deposit(amount);
    }

    // Withdraw money from account
    public synchronized boolean withdraw(String accNo, BigDecimal amount) {
        Account a = getRequired(accNo);
        return a.withdraw(amount);
    }

    // Transfer money between accounts
    public synchronized boolean transfer(String fromAcc, String toAcc, BigDecimal amount) {
        if (fromAcc.equals(toAcc)) throw new IllegalArgumentException("Cannot transfer to the same account");
        Account from = getRequired(fromAcc);
        Account to = getRequired(toAcc);
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (from.getBalance().compareTo(amount) < 0) return false; // not enough funds
        from.transferOut(amount, toAcc); // from account
        to.transferIn(amount, fromAcc); // to account
        return true;
    }

    // close account 
    public synchronized boolean closeAccount(String accNo) {
        return accounts.remove(accNo) != null;
    }

    // list all accounts sorted by account number
    public Collection<Account> listAccounts() {
        List<Account> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparing(Account::getAccountNumber));
        return list;
    }

    // Get account or throw exception if not found
    private Account getRequired(String accNo) {
        Account a = accounts.get(accNo);
        if (a == null) throw new IllegalArgumentException("Account not found: " + accNo);
        return a;
    }

    // ---------- Export ----------
    // export transaction history ti CSV
    public boolean exportStatementCsv(String accNo, String filePath) {
        Account a = accounts.get(accNo);
        if (a == null) return false;
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("Timestamp,Type,Amount,BalanceAfter,Note");
            for (Transaction t : a.getTransactions()) pw.println(t.toCsvRow());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
