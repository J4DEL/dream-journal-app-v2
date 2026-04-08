package com.jade.dreamjournalv2;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoManager {

    // --- THE SETTINGS ---
    private static final int ITERATIONS = 600000; // How many times we blend the password
    private static final int KEY_LENGTH = 256;    // AES-256 (Military Grade)
    private static final String KDF_ALGO = "PBKDF2WithHmacSHA256"; // The Blender Algorithm
    private static final String ENCRYPT_ALGO = "AES/CBC/PKCS5Padding"; // The Lockbox Algorithm

    /**
     * The Master Blender: Turns a human password into a 256-bit AES key.
     *
     * @param password The user's password
     * @param salt A unique salt to make the key extra secure
     * @return A SecretKeySpec that can be used for AES encryption/decryption
     */
    private static SecretKeySpec deriveKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KDF_ALGO);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Encrypts the dream text using the user's password.
     *
     * @param plainText The original dream text to encrypt
     * @param password The user's password to lock the dream
     * @return A combined string of Salt, IV, and EncryptedText that can be stored safely
     */
    public static String encrypt(String plainText, String password) throws Exception {
        SecureRandom random = new SecureRandom();

        // 1. Generate a random Salt
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // 2. Generate a random IV (Initialization Vector)
        byte[] iv = new byte[16];
        random.nextBytes(iv);

        // 3. Put the password and salt in the blender to get the Key
        SecretKeySpec key = deriveKey(password, salt);

        // 4. Set up the AES Lockbox
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        // 5. Encrypt the dream!
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        // 6. Package it all together so we can decrypt it later
        // Format: Base64(Salt) : Base64(IV) : Base64(EncryptedText)
        return Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(iv) + ":" +
                Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts the gibberish back into a readable dream using the password.
     *
     * @param packagedText The combined string of Salt, IV, and EncryptedText
     * @param password The user's password to unlock the dream
     * @return The original dream text if the password is correct, otherwise throws an exception
     */
    public static String decrypt(String packagedText, String password) throws Exception {
        // 1. Split the package back into its 3 parts (Salt, IV, Ciphertext)
        String[] parts = packagedText.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] iv = Base64.getDecoder().decode(parts[1]);
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[2]);

        // 2. Put the password and the recovered salt back in the blender
        SecretKeySpec key = deriveKey(password, salt);

        // 3. Unlock the AES Lockbox
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        // 4. Decrypt and return the original text
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}