package com.example.sacarolha.util.handlers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHandler {
    private static final String ALGORITHM = "SHA-256";

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] hashedBytes = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
