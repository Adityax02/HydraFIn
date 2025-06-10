package ui;

import dao.TransactionDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Transaction;

import java.time.LocalDate;
import java.util.List;

public class MainUI extends Application {

    private Label balanceLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        // Title
        Label titleLabel = new Label("HydraFin");
        titleLabel.setFont(Font.font("Segoe UI", 28));
        titleLabel.setTextFill(Color.web("#ffffff"));

        Label subTitle = new Label("Add a new transaction");
        subTitle.setFont(Font.font("Segoe UI", 14));
        subTitle.setTextFill(Color.web("#ffffffa0"));

        // Inputs
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setFont(Font.font("Segoe UI", 14));

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Food", "Rent", "Transport", "Other");
        categoryBox.setPromptText("Select category");

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton incomeRadio = new RadioButton("Income");
        RadioButton expenseRadio = new RadioButton("Expense");
        incomeRadio.setToggleGroup(typeGroup);
        expenseRadio.setToggleGroup(typeGroup);
        incomeRadio.setSelected(true);

        incomeRadio.setFont(Font.font("Segoe UI", 14));
        incomeRadio.setTextFill(Color.WHITE);

        expenseRadio.setFont(Font.font("Segoe UI", 14));
        expenseRadio.setTextFill(Color.WHITE);

        HBox typeBox = new HBox(20, incomeRadio, expenseRadio);
        typeBox.setAlignment(Pos.CENTER_LEFT);

        TextField noteField = new TextField();
        noteField.setPromptText("Optional note");
        noteField.setFont(Font.font("Segoe UI", 14));

        Button submitButton = new Button("➕ Add");
        submitButton.setFont(Font.font("Segoe UI", 14));
        submitButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #ff4ecd, #38b6ff);
            -fx-text-fill: white;
            -fx-padding: 10 25;
            -fx-background-radius: 12;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.2), 10, 0.2, 0, 3);
        """);

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Segoe UI", 13));
        statusLabel.setTextFill(Color.WHITE);

        // Balance label
        balanceLabel.setFont(Font.font("Segoe UI", 16));
        balanceLabel.setTextFill(Color.LIME);
        updateBalance();

        // Transactions Table (initially hidden)
        TableView<Transaction> table = new TableView<>();
        table.setVisible(false);

        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));

        TableColumn<Transaction, String> noteCol = new TableColumn<>("Note");
        noteCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNote()));

        table.getColumns().addAll(categoryCol, typeCol, amountCol, noteCol);

        // Show/Hide button
        Button toggleTableBtn = new Button("📋 Show Transactions");
        toggleTableBtn.setFont(Font.font("Segoe UI", 13));
        toggleTableBtn.setStyle("""
            -fx-background-color: #2d88ff;
            -fx-text-fill: white;
            -fx-background-radius: 10;
            -fx-padding: 8 20;
        """);

        toggleTableBtn.setOnAction(e -> {
            if (table.isVisible()) {
                table.setVisible(false);
                toggleTableBtn.setText("📋 Show Transactions");
            } else {
                loadTransactions(table);
                table.setVisible(true);
                toggleTableBtn.setText("❌ Hide Transactions");
            }
        });

        // Submit logic
        submitButton.setOnAction(e -> {
            try {
                Transaction t = new Transaction();
                t.setUserId(1);
                t.setAmount(Double.parseDouble(amountField.getText()));
                t.setCategory(categoryBox.getValue());
                t.setType(incomeRadio.isSelected() ? "income" : "expense");
                t.setNote(noteField.getText());
                t.setDate(LocalDate.now());

                TransactionDAO dao = new TransactionDAO();
                dao.addTransaction(t);
                statusLabel.setText("✅ Transaction added!");
                statusLabel.setTextFill(Color.LIME);

                amountField.clear();
                categoryBox.getSelectionModel().clearSelection();
                incomeRadio.setSelected(true);
                noteField.clear();

                updateBalance();
                if (table.isVisible()) loadTransactions(table);

            } catch (Exception ex) {
                statusLabel.setText("❌ Error: " + ex.getMessage());
                statusLabel.setTextFill(Color.ORANGERED);
            }
        });

        VBox form = new VBox(15,
                titleLabel, subTitle,
                amountField,
                categoryBox,
                typeBox,
                noteField,
                submitButton,
                statusLabel,
                balanceLabel,
                toggleTableBtn,
                table
        );
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(30));
        form.setMaxWidth(400);
        form.setStyle("""
            -fx-font-family: 'Segoe UI', 'Helvetica Neue', 'Arial', sans-serif;
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: rgba(255,255,255,0.2);
            -fx-border-width: 1.2;
        """);

        StackPane root = new StackPane(form);
        root.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);
        """);

        Scene scene = new Scene(root, 440, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("HydraFin");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void updateBalance() {
        TransactionDAO dao = new TransactionDAO();
        List<Transaction> transactions = dao.getAllTransactions(1);

        double income = transactions.stream()
                .filter(t -> "income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();

        double expense = transactions.stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();

        double total = income - expense;
        balanceLabel.setText("💰 Balance: ₹" + total);
    }

    private void loadTransactions(TableView<Transaction> table) {
        TransactionDAO dao = new TransactionDAO();
        List<Transaction> transactions = dao.getAllTransactions(1);
        table.getItems().setAll(transactions);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
