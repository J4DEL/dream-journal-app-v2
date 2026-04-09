package com.jade.dreamjournalv2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // The path where your database file will be created
    private static final String DB_URL = "jdbc:sqlite:madjs_data.db";

    /**
     * Method to establish a connection to the SQLite database.
     * @return
     */
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

    /**
     * Method to initialize the database with the necessary table structure for storing dreams.
     * This method creates a table called 'dreams' with columns for:
     * - id: A unique identifier for each dream (auto-incremented)
     * - content: The encrypted dream text (cannot be null)
     * - is_lucid: A boolean to track if the dream was lucid or not (cannot be null)
     * - date_logged: A timestamp of when the dream was saved (auto-stamped with the current date and time)
     * - hours_slept: An integer to track how many hours I slept before the dream
     * - mood_before_sleep: A text field to track my mood before sleeping
     * - mood_after_sleep: A text field to track my mood after waking up
     * - dream_signs: A text field to track any dream signs I noticed in the dream
     * - method_used: A text field to track which lucid dreaming method I used (if any)
     * - substances: A text field to track any substances I consumed before sleeping (e.g. vyvanse, alcohol, zaza)
     * - environment: A text field to track the sleeping environment (e.g. room temperature, noise level, bedding)
     * - themes: A text field to track recurring themes in my dreams (e.g. flying, being chased, school)
     * - false_awakenings: An integer to track how many times I experienced false awakenings in the dream
     * - is_normal: A boolean to track if the dream was a normal non-lucid dream
     * - is_nightmare: A boolean to track if the dream was a nightmare
     * - is_wet: A boolean to track wet dreams
     * - mads: A boolean to track if I experienced MADs (my version of sleep paralysis hallucinations) in the dream
     * - paralysis: A boolean to track if I experienced sleep paralysis in the dream
     * - date_logged: A timestamp to track when the dream was logged (auto-stamped with the current date and time)
     */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS dreams (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " content TEXT NOT NULL,\n"
                + " title TEXT,\n"
                + " hours_slept INTEGER,\n"
                + " quality INTEGER,\n"
                + " sleep_time TEXT,\n"
                + " wake_time TEXT,\n"
                + " mood_before TEXT,\n"
                + " mood_after TEXT,\n"
                + " method_used TEXT,\n"
                + " substances TEXT,\n"
                + " dream_signs TEXT,\n"
                + " environment TEXT,\n"
                + " themes TEXT,\n"
                + " false_awakenings INTEGER,\n"
                + " is_lucid BOOLEAN,\n"
                + " is_normal BOOLEAN,\n"
                + " is_nightmare BOOLEAN,\n"
                + " is_wet BOOLEAN,\n"
                + " mads BOOLEAN,\n"
                + " paralysis BOOLEAN,\n"
                + " date_logged DATETIME DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        // Create a separate table just for app settings (like the master password)
        String settingsSql = "CREATE TABLE IF NOT EXISTS settings (id INTEGER PRIMARY KEY, password_hash TEXT);";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // 1. Create the dreams table
            stmt.execute(sql);

            // 2. Create the settings table
            stmt.execute(settingsSql);

            System.out.println("Mega-Vault database is secure and ready!");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    /**
     * Method to insert a new dream entry into the database. This method takes in all the relevant data about the dream and saves it as a new record in the 'dreams' table.
     * @param content The encrypted dream text to be stored in the database (cannot be null)
     * @param title The title of the dream
     * @param hours The hours slept
     * @param quality The quality of sleep out of 10
     * @param sleepTime Time I slept
     * @param wakeTime Time I woke up
     * @param moodB Mood after wake
     * @param moodA Mood before sleep
     * @param method Lucid dream method
     * @param subs Substances taken that night
     * @param signs dream signs
     * @param env environment in the dream
     * @param themes dream themes
     * @param fAwake number of false awakenings
     * @param lucid whether I was lucid
     * @param normal normal dream
     * @param nightmare nightmare
     * @param wet wet dream
     * @param mads mads was there
     * @param paralysis sleep paralysis
     */
    public static void insertDream(String content, String title, int hours, int quality,
                                   String sleepTime, String wakeTime, String moodB,
                                   String moodA, String method, String subs, String signs,
                                   String env, String themes, int fAwake, boolean lucid,
                                   boolean normal, boolean nightmare, boolean wet,
                                   boolean mads, boolean paralysis) {

        // 20 Question marks for 20 pieces of data!
        String sql = "INSERT INTO dreams(content, title, hours_slept, quality, sleep_time, wake_time, mood_before, mood_after, method_used, substances, dream_signs, environment, themes, false_awakenings, is_lucid, is_normal, is_nightmare, is_wet, mads, paralysis) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, content);
            pstmt.setString(2, title);
            pstmt.setInt(3, hours);
            pstmt.setInt(4, quality);
            pstmt.setString(5, sleepTime);
            pstmt.setString(6, wakeTime);
            pstmt.setString(7, moodB);
            pstmt.setString(8, moodA);
            pstmt.setString(9, method);
            pstmt.setString(10, subs);
            pstmt.setString(11, signs);
            pstmt.setString(12, env);
            pstmt.setString(13, themes);
            pstmt.setInt(14, fAwake);
            pstmt.setBoolean(15, lucid);
            pstmt.setBoolean(16, normal);
            pstmt.setBoolean(17, nightmare);
            pstmt.setBoolean(18, wet);
            pstmt.setBoolean(19, mads);
            pstmt.setBoolean(20, paralysis);

            pstmt.executeUpdate();
            System.out.println("SUCCESS: Massive dream file locked in the vault!");

        } catch (SQLException e) {
            System.out.println("Error saving dream: " + e.getMessage());
        }
    }

    // --- PASSWORD MANAGEMENT ---

    // 1. Check if a password exists yet
    public static boolean hasMasterPassword() {
        String sql = "SELECT password_hash FROM settings WHERE id = 1";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() && rs.getString("password_hash") != null;
        } catch (SQLException e) {
            return false;
        }
    }

    // 2. Save the password hash (Only happens on first boot)
    public static void saveMasterPassword(String hash) {
        String sql = "INSERT INTO settings (id, password_hash) VALUES (1, ?)";
        try (Connection conn = connect(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hash);
            pstmt.executeUpdate();
            System.out.println("Master Password hash secured!");
        } catch (SQLException e) {
            System.out.println("Error saving password: " + e.getMessage());
        }
    }

    // 3. Get the hash to verify a login attempt
    public static String getMasterPasswordHash() {
        String sql = "SELECT password_hash FROM settings WHERE id = 1";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getString("password_hash");
        } catch (SQLException e) {
            System.out.println("Error retrieving password hash: " + e.getMessage());
        }
        return null;
    }

    // --- ARCHIVE MANAGEMENT ---

    // 4. Fetch all dreams to display in the Archive List
    public static java.util.List<DreamEntry> getAllDreams() {
        java.util.List<DreamEntry> dreamList = new java.util.ArrayList<>();

        // Grab the ID, Title, Date, and Encrypted Text, ordered by newest first!
        String sql = "SELECT id, title, date_logged, content FROM dreams ORDER BY date_logged DESC";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dreamList.add(new DreamEntry(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date_logged"),
                        rs.getString("content")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading archive: " + e.getMessage());
        }
        return dreamList;
    }

    // 5. Permanently wipe a dream from the database
    public static void deleteDream(int id) {
        String sql = "DELETE FROM dreams WHERE id = ?";
        try (Connection conn = connect(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Dream permanently deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting dream: " + e.getMessage());
        }
    }
}