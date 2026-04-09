package com.jade.dreamjournalv2;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    // --- THE SAVE BUTTON ---
    @FXML
    protected void onSaveClick() {
        System.out.println("Save Button Clicked! Ready to lock the dream.");
    }
}