package com.jade.dreamjournalv2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;

public class ArchiveController {

    // --- 1. UI LINKS ---
    @FXML private ListView<DreamEntry> dreamListView;
    @FXML private TextArea dreamReader;

    // --- 2. BOOT UP SEQUENCE ---
    @FXML
    public void initialize() {
        // 1. Fetch all dreams from the database
        java.util.List<DreamEntry> dreams = DatabaseManager.getAllDreams();

        // 2. Load them into the scrolling sidebar
        dreamListView.getItems().addAll(dreams);

        // 3. THE LISTENER: This watches the ListView. Whenever you click a dream, it runs this code!
        dreamListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                readDream(newValue);
            }
        });
    }

    // --- 3. DECRYPT AND DISPLAY ---
    private void readDream(DreamEntry selectedDream) {
        try {
            String password = UserSession.getKey();
            String decryptedText = CryptoManager.decrypt(selectedDream.getEncryptedContent(), password);

            // Fetch that massive stitched header we just built!
            String megaHeader = selectedDream.buildMetadataHeader();

            // Slap it all into the massive reader canvas
            dreamReader.setText(megaHeader + decryptedText);

        } catch (Exception e) {
            dreamReader.setText("ERROR: Could not decrypt this dream. Incorrect Master Key or corrupted file.");
            System.out.println("Decryption failed: " + e.getMessage());
        }
    }

    // --- 4. THE DESTRUCTIVE DELETE BUTTON ---
    @FXML
    protected void onDeleteClick() {
        // Find out which dream is currently highlighted
        DreamEntry selectedDream = dreamListView.getSelectionModel().getSelectedItem();

        if (selectedDream == null) {
            return; // If they clicked delete but didn't highlight a dream, do nothing
        }

        // Summon the Safety Lock!
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Security Warning");
        alert.setHeaderText("Initiating Permanent Deletion");
        alert.setContentText("Are you absolutely sure you want to burn '" + selectedDream.getTitle() + "'? This action cannot be undone.");

        // Wait for them to click OK or Cancel
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 1. Delete from database
            DatabaseManager.deleteDream(selectedDream.getId());

            // 2. Remove it from the UI sidebar
            dreamListView.getItems().remove(selectedDream);

            // 3. Clear the reader screen
            dreamReader.clear();

            System.out.println("Dream incinerated.");
        }
    }

    // --- 5. BACK TO VAULT BUTTON ---
    @FXML
    protected void onBackClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DreamJournalApplication.class.getResource("vault-view.fxml"));
            // Swaps the canvas back to the dashboard instantly
            dreamListView.getScene().setRoot(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Error returning to vault: " + e.getMessage());
        }
    }
}