package util;

import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("✅ Connection Successful!");
        } else {
            System.out.println("❌ Connection Failed!");
        }
    }
}
