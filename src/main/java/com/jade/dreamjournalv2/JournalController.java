package com.jade.dreamjournalv2;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class JournalController {

    @FXML
    private PasswordField passwordInput;

    @FXML
    protected void onEnterClick() {
        // Grab the text from the password box
        String enteredPassword = passwordInput.getText();

        // For testing ONLY: Print it to the console so we know it works
        System.out.println("Vault checking password: " + enteredPassword);

        // We will eventually delete this print statement because
        // printing passwords is a cybersecurity sin!
    }
}