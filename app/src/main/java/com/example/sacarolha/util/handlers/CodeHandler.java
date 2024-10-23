package com.example.sacarolha.util.handlers;

import java.security.SecureRandom;

public class CodeHandler {

    public static String generateUniqueNumberHash() {
        SecureRandom random = new SecureRandom();
        long currentTimeMillis = System.currentTimeMillis() % 10000000000L;
        int randomNum = random.nextInt(100000);

        String hash = String.valueOf(currentTimeMillis) + String.valueOf(randomNum);

        if (hash.length() > 10) {
            hash = hash.substring(hash.length() - 10);
        } else if (hash.length() < 10) {
            hash = String.format("%010d", Long.parseLong(hash));
        }

        return hash;
    }
}
