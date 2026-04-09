package com.jade.dreamjournalv2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class VaultController {

    // --- 1. THE TEXT AREAS & FIELDS ---
    @FXML private TextArea dreamInput;
    @FXML private TextField dreamTitleInput;
    @FXML private TextField hoursInput;
    @FXML private TextField qualityInput;
    @FXML private TextField sleepTimeInput;
    @FXML private TextField wakeTimeInput;
    @FXML private TextField signsInput;
    @FXML private TextField substanceInput;
    @FXML private TextField environmentInput;
    @FXML private TextField themesInput;
    @FXML private TextField fAwakenInput;

    // --- 2. THE DROPDOWNS ---
    @FXML private ComboBox<String> moodBeforeInput;
    @FXML private ComboBox<String> moodAfterInput;
    @FXML private ComboBox<String> methodUsedInput;

    // --- 3. THE CHECKBOXES ---
    @FXML private CheckBox lucidToggle;
    @FXML private CheckBox normToggle;
    @FXML private CheckBox nightmareToggle;
    @FXML private CheckBox wetToggle;
    @FXML private CheckBox madsToggle;
    @FXML private CheckBox paralysisToggle;

    // Save button
    @FXML private Button saveBtn;

    // --- BOOT UP SEQUENCE ---
    @FXML
    public void initialize() {
        moodBeforeInput.getItems().addAll("Calm", "Anxious", "Exhausted", "Excited", "Neutral", "Happy", "Angry", "Stressed", "Scared", "Sad", "Comfort", "Love", "Horny", "Peaceful", "Indescribable");
        moodAfterInput.getItems().addAll("Refreshed", "Neutral", "Calm", "Groggy","Detached", "Inspired", "Confused", "Scared", "Angry", "Anxious", "Neutral", "Happy", "Sad", "Motivated", "Horny", "Peaceful", "Indescribable");

        // Populate the specific lucid dreaming methods!
        methodUsedInput.getItems().addAll("MILD", "WILD", "WBTB", "DILD", "SSILD", "None");

        moodBeforeInput.setValue("Neutral");
        moodAfterInput.setValue("Neutral");
        methodUsedInput.setValue("None");
    }

    @FXML
    protected void onSaveClick() {
        // 1. ANTI-SPAM: Disable the button the microsecond it is clicked
        saveBtn.setDisable(true);

        String rawDream = dreamInput.getText();

        // 2. ANTI-SPAM: Don't let them save if the main text area is completely empty
        if (rawDream.trim().isEmpty()) {
            System.out.println("Cannot save an empty dream!");
            saveBtn.setDisable(false); // Turn button back on
            return;
        }

        // Grab all the other strings...
        String title = dreamTitleInput.getText();
        String sleepT = sleepTimeInput.getText();
        String wakeT = wakeTimeInput.getText();
        String signs = signsInput.getText();
        String subs = substanceInput.getText();
        String env = environmentInput.getText();
        String themes = themesInput.getText();

        // Safely grab the numbers...
        int hours = 0, quality = 0, fAwake = 0;
        try {
            if (!hoursInput.getText().isEmpty()) hours = Integer.parseInt(hoursInput.getText());
            if (!qualityInput.getText().isEmpty()) quality = Integer.parseInt(qualityInput.getText());
            if (!fAwakenInput.getText().isEmpty()) fAwake = Integer.parseInt(fAwakenInput.getText());
        } catch (NumberFormatException e) {
            System.out.println("Numbers only in the number boxes!");
            saveBtn.setDisable(false);
            return;
        }

        // Grab Dropdowns & Checkboxes...
        String moodB = moodBeforeInput.getValue() != null ? moodBeforeInput.getValue() : "None";
        String moodA = moodAfterInput.getValue() != null ? moodAfterInput.getValue() : "None";
        String method = methodUsedInput.getValue() != null ? methodUsedInput.getValue() : "None";
        boolean isLucid = lucidToggle.isSelected();
        boolean isNorm = normToggle.isSelected();
        boolean isNightmare = nightmareToggle.isSelected();
        boolean isWet = wetToggle.isSelected();
        boolean isMads = madsToggle.isSelected();
        boolean isPara = paralysisToggle.isSelected();

        // 3. SILENT SECURITY: Grab the password from the SessionManager!
        String password = UserSession.getKey();

        try {
            // Encrypt and Save
            String encryptedDream = CryptoManager.encrypt(rawDream, password);
            DatabaseManager.insertDream(encryptedDream, title, hours, quality, sleepT, wakeT,
                    moodB, moodA, method, subs, signs, env, themes, fAwake,
                    isLucid, isNorm, isNightmare, isWet, isMads, isPara);

            // 4. ANTI-SPAM: Wipe EVERY single UI element clean
            dreamInput.clear();
            dreamTitleInput.clear();
            hoursInput.clear();
            qualityInput.clear();
            sleepTimeInput.clear();
            wakeTimeInput.clear();
            signsInput.clear();
            substanceInput.clear();
            environmentInput.clear();
            themesInput.clear();
            fAwakenInput.clear();

            // Uncheck all checkboxes
            lucidToggle.setSelected(false);
            normToggle.setSelected(false);
            nightmareToggle.setSelected(false);
            wetToggle.setSelected(false);
            madsToggle.setSelected(false);
            paralysisToggle.setSelected(false);

            // Reset dropdowns to their default states
            moodBeforeInput.setValue("Neutral");
            moodAfterInput.setValue("Neutral");
            methodUsedInput.setValue("None");

            System.out.println("Dream securely saved to Vault! Form wiped clean.");

        } catch (Exception e) {
            System.out.println("Encryption failed! " + e.getMessage());
        } finally {
            // 5. ANTI-SPAM: Turn the button back on so they can log their next dream
            saveBtn.setDisable(false);
        }
    }

    // --- OPEN ARCHIVE BUTTON ---
    @FXML
    protected void onArchiveClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DreamJournalApplication.class.getResource("archive-view.fxml"));
            // We just swap the root canvas, we don't touch the window!
            saveBtn.getScene().setRoot(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Error loading archive: " + e.getMessage());
        }
    }
}