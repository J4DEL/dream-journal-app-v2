package com.jade.dreamjournalv2;

public class UserSession {
    // This variable lives in temporary RAM. It dies when the app closes.
    private static String masterKey = null;

    // Call this when you log in
    public static void login(String password) {
        masterKey = password;
    }

    // Call this when the Vault needs the key to encrypt a dream
    public static String getKey() {
        return masterKey;
    }

    // Call this if you ever build a "Log Out" button
    public static void logout() {
        masterKey = null;
    }
}