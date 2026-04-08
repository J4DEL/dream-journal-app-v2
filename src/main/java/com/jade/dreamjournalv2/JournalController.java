package com.jade.dreamjournalv2;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class JournalController {

    @FXML
    private PasswordField passwordInput;

    @FXML
    protected void onEnterClick() {
        String enteredPassword = passwordInput.getText();

        try {
            // A fake dream to test
            String myDream = "I was flying over Night City and realized I was dreaming!";

            System.out.println("--- TESTING CRYPTO ENGINE ---");
            System.out.println("Original: " + myDream);

            // Lock it!
            String lockedDream = CryptoManager.encrypt(myDream, enteredPassword);
            System.out.println("Encrypted Package: " + lockedDream);

            // Unlock it!
            String unlockedDream = CryptoManager.decrypt(lockedDream, enteredPassword);
            System.out.println("Decrypted: " + unlockedDream);

        } catch (Exception e) {
            System.out.println("Crypto failed: " + e.getMessage());
        }
    }
}