# Bank Management System using Java

## Overview
This project is a **Java-based GUI Bank Management System** that allows users to create and manage bank accounts, perform transactions, and track account history. It uses **Java Swing** for the graphical interface and file-based storage for account data.  

---

## What We Are Going To Do
- Create a system to manage multiple bank accounts.  
- Allow users to securely login using account number and PIN.  
- Perform core banking operations like deposit, withdrawal, and transfer.  
- Track and display transaction history.  
- Export transaction statements to CSV.  
- Provide a user-friendly GUI interface for easy navigation.

---

## Folder Structure

Bank_Management_System_using_Java/

├── Account.java
├── Bank.java
├── BankGUIApp.java
├── Transaction.java
├── logo1.png
└── .gitignore

---

**Extra Files Generated After Compile / Run:**  
- `accounts.dat` – Stores saved account data at runtime. Needed for persistence.  
- `TransactionType.class` – Compiled enum used by `Transaction.java`.  

---

## Technologies Used
- **Java SE** – Core programming language  
- **Swing** – GUI framework for building the application interface  
- **File I/O** – For saving and loading account data  
- **SHA-256** – Hashing algorithm for secure PIN storage  

---

## Features Added
- Create bank accounts with optional initial deposit.  
- Secure login with PIN authentication.  
- Deposit, withdraw, and transfer funds.  
- View account balance and transaction history.  
- Export transaction history to CSV.  
- Close account permanently.  
- Modern GUI design with buttons, menus, and logo.  

---

## Commands to Run the Program
1. Clone the repository:
git clone https://github.com/VarshaVardhiniM/Bank_Management_System_using_Java.git

2. Navigate to the project folder:
cd Bank_Management_System_using_Java

3. Compile the Java files:
javac *.java

4. Run the program:
java BankGUIApp

Now Folder Structure looks like:

Bank_Management_System_using_Java/


├─ Account.java
├─ Account.class
├─ Bank.java
├─ Bank.class
├─ BankGUIApp.java
├─ BankGUIApp.class
├─ BankGUIApp$1.class
├─ Transaction.java
├─ Transaction.class
├─ TransactionType.class
├─ logo1.png
├─ accounts.dat
└─ .gitignore

---

## Extra Files and Their Uses
accounts.dat – Runtime file that stores account information. Used to load/save accounts when the application starts or exits.
TransactionType.class – Compiled enum representing different types of transactions (DEPOSIT, WITHDRAWAL, etc.). Used internally by Transaction.java.

---

## Final Conclusion
This Bank Management System project demonstrates the implementation of core banking operations using Java and Swing. It highlights:
-> Object-oriented programming concepts (Account, Bank, Transaction).
-> Secure handling of sensitive data using hashing.
-> File-based persistence and CSV export.
-> Building a complete, interactive GUI application.
It is suitable for learning Java, GUI design, and basic banking system logic.

---

If you want, you can also **make it more visually appealing for GitHub** by adding **badges, screenshots, and a small demo GIF** so your repo looks professional and ready to showcase.  

---

### ~Manthripragada Varsha Vardhini

