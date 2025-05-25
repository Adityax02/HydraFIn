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

public class MainUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title
        Label titleLabel = new Label("HydraFin");
        titleLabel.setFont(Font.font("Georgia", 28));
        titleLabel.setTextFill(Color.web("#ffffff"));

        Label subTitle = new Label("Add a new transaction");
        subTitle.setFont(Font.font("Georgia", 14));
        subTitle.setTextFill(Color.web("#ffffffa0"));

        // Inputs
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setFont(Font.font("Georgia", 14));

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Food", "Rent", "Transport", "Other");
        categoryBox.setPromptText("Select category");
        categoryBox.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 14;");

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton incomeRadio = new RadioButton("Income");
        RadioButton expenseRadio = new RadioButton("Expense");
        incomeRadio.setToggleGroup(typeGroup);
        expenseRadio.setToggleGroup(typeGroup);
        incomeRadio.setSelected(true);

        incomeRadio.setFont(Font.font("Georgia", 14));
        incomeRadio.setTextFill(Color.WHITE);

        expenseRadio.setFont(Font.font("Georgia", 14));
        expenseRadio.setTextFill(Color.WHITE);


        HBox typeBox = new HBox(20, incomeRadio, expenseRadio);
        typeBox.setAlignment(Pos.CENTER_LEFT);

        TextField noteField = new TextField();
        noteField.setPromptText("Optional note");
        noteField.setFont(Font.font("Georgia", 14));

        // Vibrant Glass Button
        Button submitButton = new Button("➕ Add");
        submitButton.setFont(Font.font("Georgia", 14));
        submitButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #ff4ecd, #38b6ff);
            -fx-text-fill: white;
            -fx-padding: 10 25;
            -fx-background-radius: 12;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.2), 10, 0.2, 0, 3);
        """);

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Georgia", 13));
        statusLabel.setTextFill(Color.WHITE);

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
            } catch (Exception ex) {
                statusLabel.setText("❌ Error: " + ex.getMessage());
                statusLabel.setTextFill(Color.ORANGERED);
            }
        });

        // Glassmorphic container
        VBox form = new VBox(15,
                titleLabel, subTitle,
                amountField,
                categoryBox,
                typeBox,
                noteField,
                submitButton,
                statusLabel
        );
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(30));
        form.setMaxWidth(350);
        form.setStyle("""
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

        Scene scene = new Scene(root, 420, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("HydraFin");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
