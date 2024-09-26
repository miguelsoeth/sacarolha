package com.example.sacarolha.util.handlers;

public class StringHandler {
    public static String capitalize(String str) {
        String[] words = str.split(" ");
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return capitalizedString.toString().trim(); // Remove trailing space
    }
}
