package gabriel.prog.mediavault.test;

import gabriel.prog.mediavault.dao.DataBaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Testing database connection...");

            // Try to get connection
            Connection conn = DataBaseConnection.getInstance().getConnection();
            System.out.println("Connection established successfully!");

            // Test query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 as test");
            if (rs.next()) {
                System.out.println("Query executed successfully! Result: " + rs.getInt("test"));
            }

            // Check if media table exists
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM media");
            if (rs.next()) {
                System.out.println("Media table exists! Row count: " + rs.getInt("count"));
            }

        } catch (Exception e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}