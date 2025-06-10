# 💸 HydraFin - Personal Finance Tracker

HydraFin is a JavaFX-based GUI application that allows users to track their personal income and expenses. Built with a modular architecture and connected to a MySQL database, the app ensures a clean, functional, and user-friendly experience.

---

## 🚀 Features

- Add new transactions (income or expense)
- View all past transactions in a structured table
- Displays total balance (Income - Expense)
- Smooth event handling and form validations
- Beautiful, glassmorphic UI
- Modular architecture using MVC pattern

---

## 🧩 Tech Stack

- **Java 17**
- **JavaFX**
- **MySQL** (via JDBC)
- **Maven** (for dependencies if needed)
- Clean OOP structure with packages: `model`, `dao`, `util`, `ui`

---

## 📁 Project Structure

HydraFIn/
├── model/ # Data model classes
│ └── Transaction.java
├── dao/ # Database access
│ └── TransactionDAO.java
├── util/ # Utility classes
│ ├── DBConnection.java
│ └── DBTest.java
├── ui/ # JavaFX UI
│ └── MainUI.java
├── Main.java # JavaFX launch file (test UI)
└── README.md # You're here!


---

## 🧪 Setup Instructions

1. **Install Requirements**
   - Java 17+
   - MySQL (e.g., via XAMPP)
   - JavaFX SDK

2. **Database Setup**
   - Create a MySQL database named `hydrafin`
   - Run this SQL to create the table:

     ```sql
     CREATE TABLE transactions (
         txn_id INT AUTO_INCREMENT PRIMARY KEY,
         user_id INT,
         amount DOUBLE,
         category VARCHAR(50),
         type VARCHAR(20),
         date DATE,
         note TEXT
     );
     ```

3. **Configure DB Credentials**
   - In `DBConnection.java`, ensure username/password match your MySQL setup.

4. **Run the App**
   - Set `MainUI.java` as the main class in your IDE (like IntelliJ)
   - Run and start adding transactions!

---
