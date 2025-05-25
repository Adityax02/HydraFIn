package dao;

import model.Transaction;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDAO {

    public void addTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (user_id, amount, category, type, date, note) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getUserId());
            ps.setDouble(2, t.getAmount());
            ps.setString(3, t.getCategory());
            ps.setString(4, t.getType());
            ps.setDate(5, java.sql.Date.valueOf(t.getDate()));
            ps.setString(6, t.getNote());

            ps.executeUpdate();
            System.out.println("✅ Transaction added successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Failed to add transaction");
            e.printStackTrace();
        }
    }
}
