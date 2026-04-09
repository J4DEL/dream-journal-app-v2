package com.jade.dreamjournalv2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class JournalController {

    @FXML private PasswordField passwordInput;
    @FXML private Label titleLabel;
    @FXML private Button enterBtn;

    private boolean isSetupMode = false;

    // This runs the moment the tiny window opens
    @FXML
    public void initialize() {
        // Ask the database: Do we have a password yet?
        if (!DatabaseManager.hasMasterPassword()) {
            isSetupMode = true;
            titleLabel.setText("Setup Master Key");
            enterBtn.setText("Create Key");
        } else {
            isSetupMode = false;
            titleLabel.setText("Dream Journal");
            enterBtn.setText("Enter");
        }
    }

    @FXML
    protected void onEnterClick() {
        String enteredPassword = passwordInput.getText();

        if (enteredPassword.isEmpty()) return;

        if (isSetupMode) {
            // --- FIRST TIME SETUP ---
            // 1. Hash it and save it to the DB forever
            String hash = CryptoManager.hashPassword(enteredPassword);
            DatabaseManager.saveMasterPassword(hash);

            // 2. Log them in to the current session
            UserSession.login(enteredPassword);
            System.out.println("Setup complete. Welcome.");
            loadDashboard();

        } else {
            // --- REGULAR LOGIN ---
            // 1. Hash what they just typed
            String attemptHash = CryptoManager.hashPassword(enteredPassword);
            String savedHash = DatabaseManager.getMasterPasswordHash();

            // 2. Compare the hashes
            if (attemptHash != null && attemptHash.equals(savedHash)) {
                System.out.println("Access Granted.");
                UserSession.login(enteredPassword);
                loadDashboard();
            } else {
                System.out.println("ACCESS DENIED: Incorrect Password.");
                passwordInput.clear();
                passwordInput.setPromptText("Incorrect Password");
                passwordInput.setStyle("-fx-border-color: red;"); // Turn box red on failure
            }
        }
    }

    // Helper method to jump to the dashboard
    private void loadDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DreamJournalApplication.class.getResource("vault-view.fxml"));
            Scene vaultScene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) passwordInput.getScene().getWindow();
            stage.setScene(vaultScene);
            stage.setMaximized(true);
        } catch (Exception e) {
            System.out.println("Error loading vault: " + e.getMessage());
        }
    }
}