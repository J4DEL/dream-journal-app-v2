package com.jade.dreamjournalv2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // The path where your database file will be created
    private static final String DB_URL = "jdbc:sqlite:madjs_data.db";

    // 1. Method to connect to the database
    public static Connection connect() {
        Connection conn = null;
        try {
            // If madjs_data.db doesn't exist, SQLite creates it automatically right here!
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }

    // 2. Method to build the vault structure
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS dreams (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" // Gives every dream a unique number
                + " content TEXT NOT NULL,\n"               // Where the encrypted dream goes
                + " is_lucid BOOLEAN NOT NULL,\n"            // True/False for your analytics
                + " date_logged DATETIME DEFAULT CURRENT_TIMESTAMP\n" // Auto-stamps the date
                + " hours_slept INTEGER,\n" // Track how many hours I slept
                + " mood_before_sleep TEXT,\n" // Track my mood before sleeping
                + " mood_after_sleep TEXT,\n" // Track mood after waking up
                + " dream_signs TEXT,\n" // Track dream signs
                + ");";

        // Try to connect and execute the SQL
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Vault database is secure and ready!");

        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
}