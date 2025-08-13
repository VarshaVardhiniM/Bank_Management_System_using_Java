import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

//GUI-based Bank application
public class BankGUIApp {
    private static final String DATA_FILE = "accounts.dat"; // Data file for persistence
    private Bank bank; // Bank object managing accounts

    private JFrame frame; // Main application window
    private JPanel mainPanel; // Currently displayed panel

    // Modern color palette
    private final Color primaryColor = new Color(52, 152, 219);
    private final Color secondaryColor = new Color(245, 246, 250);
    private final Color accentColor = new Color(46, 204, 113);
    private final Color dangerColor = new Color(231, 76, 60);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankGUIApp::new); // Launch GUI in Event dispatch thread
    }

    public BankGUIApp() {
        bank = Bank.load(DATA_FILE); // Load saved bank data or initialize new
        frame = new JFrame("Bank Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        showMainMenu();
        frame.setVisible(true);
    }

    // Styled button with hover effect
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private void showMainMenu() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240)); // Solid light background

        // Top panel with scaled logo
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Load and resize logo
        ImageIcon originalIcon = new ImageIcon("logo1.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledIcon, SwingConstants.CENTER);
        topPanel.add(logoLabel, BorderLayout.CENTER);

        // Center menu with 3 buttons
        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 60, 200));

        JButton createBtn = createStyledButton("➕ Create Account", accentColor);
        JButton loginBtn = createStyledButton("🔑 Login", primaryColor);
        JButton exitBtn = createStyledButton("❌ Exit", dangerColor);

        // Buttons actions
        createBtn.addActionListener(e -> showCreateAccount());
        loginBtn.addActionListener(e -> showLogin());
        exitBtn.addActionListener(e -> {
            bank.save(DATA_FILE);
            frame.dispose();
        });

        menuPanel.add(createBtn);
        menuPanel.add(loginBtn);
        menuPanel.add(exitBtn);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.revalidate();
    }

    // create account dialog
    private void showCreateAccount() {
        JTextField nameField = new JTextField();
        JTextField pinField = new JTextField();
        JTextField depositField = new JTextField();

        Object[] message = {
                "Full Name:", nameField,
                "PIN (4-6 digits):", pinField,
                "Initial Deposit:", depositField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Create Account", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String pin = pinField.getText().trim();
                BigDecimal deposit = new BigDecimal(depositField.getText().trim());
                Account acc = bank.createAccount(name, pin, deposit);
                bank.save(DATA_FILE);
                JOptionPane.showMessageDialog(frame, "✅ Account created: " + acc.getAccountNumber());
                showMainMenu();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Login dialog
    private void showLogin() {
        JTextField accField = new JTextField();
        JTextField pinField = new JTextField();

        Object[] message = {
                "Account Number:", accField,
                "PIN:", pinField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Account acc = bank.authenticate(accField.getText().trim(), pinField.getText().trim());
            if (acc != null) {
                showAccountMenu(acc);
            } else {
                JOptionPane.showMessageDialog(frame, "❌ Invalid account or PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Account menu after login
    private void showAccountMenu(Account acc) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryColor);

        JLabel title = new JLabel("Welcome, " + acc.getHolderName() + " (" + acc.getAccountNumber() + ")",
                SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        buttonPanel.setBackground(secondaryColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // create buttons for account actions
        JButton balBtn = createStyledButton("💰 View Balance", primaryColor);
        JButton depBtn = createStyledButton("💵 Deposit", accentColor);
        JButton witBtn = createStyledButton("💸 Withdraw", dangerColor);
        JButton transBtn = createStyledButton("🔄 Transfer", primaryColor);
        JButton histBtn = createStyledButton("📜 Transaction History", accentColor);
        JButton exportBtn = createStyledButton("📂 Export Statement (CSV)", primaryColor);
        JButton closeBtn = createStyledButton("🗑️ Close Account", dangerColor);
        JButton logoutBtn = createStyledButton("🚪 Logout", Color.GRAY);

        // Balance
        balBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Balance: " + acc.getBalance()));
        // Deposit
        depBtn.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog(frame, "Deposit amount:");
            try {
                bank.deposit(acc.getAccountNumber(), new BigDecimal(amt));
                bank.save(DATA_FILE);
                JOptionPane.showMessageDialog(frame, "Deposited. New balance: " + acc.getBalance());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        // Withdraw
        witBtn.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog(frame, "Withdraw amount:");
            try {
                boolean ok = bank.withdraw(acc.getAccountNumber(), new BigDecimal(amt));
                bank.save(DATA_FILE);
                JOptionPane.showMessageDialog(frame,
                        ok ? "Withdrawn. New balance: " + acc.getBalance() : "Insufficient funds.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        // Transfer
        transBtn.addActionListener(e -> {
            JTextField toAcc = new JTextField();
            JTextField amt = new JTextField();
            Object[] msg = { "To Account:", toAcc, "Amount:", amt };
            int opt = JOptionPane.showConfirmDialog(frame, msg, "Transfer", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                try {
                    boolean ok = bank.transfer(acc.getAccountNumber(), toAcc.getText().trim(),
                            new BigDecimal(amt.getText().trim()));
                    bank.save(DATA_FILE);
                    JOptionPane.showMessageDialog(frame,
                            ok ? "Transferred." : "Insufficient funds or invalid account.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });
        // Transfer history
        histBtn.addActionListener(e -> {
            JTextArea area = new JTextArea();
            acc.getTransactions().forEach(t -> area.append(t + "\n"));
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(frame, scrollPane, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
        });
        // Export CSV
        exportBtn.addActionListener(e -> {
            String fileName = acc.getAccountNumber() + "_statement.csv";
            if (bank.exportStatementCsv(acc.getAccountNumber(), fileName))
                JOptionPane.showMessageDialog(frame, "✅ Exported to: " + fileName);
            else
                JOptionPane.showMessageDialog(frame, "❌ Export failed.");
        });
        // Close account
        closeBtn.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(frame, "Are you sure? This cannot be undone.", "Close Account",
                    JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                bank.closeAccount(acc.getAccountNumber());
                bank.save(DATA_FILE);
                JOptionPane.showMessageDialog(frame, "Account closed.");
                showMainMenu();
            }
        });
        // Logout
        logoutBtn.addActionListener((ActionEvent e) -> showMainMenu());

        // Add all buttons
        buttonPanel.add(balBtn);
        buttonPanel.add(depBtn);
        buttonPanel.add(witBtn);
        buttonPanel.add(transBtn);
        buttonPanel.add(histBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(closeBtn);
        buttonPanel.add(logoutBtn);

        // Assemble UI
        panel.add(title, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.revalidate();
    }
}
