package com.example.sacarolha.util.handlers;

import android.content.Context;

import com.example.sacarolha.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static String convertToDefaultDate(Context context, String dateTimeStr) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.db_date_format));
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.full_date_format));

        return dateTime.format(outputFormatter);
    }

    public static String convertToDefaultShortDate(Context context, String dateTimeStr) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.db_date_format));
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.date_format));

        return dateTime.format(outputFormatter);
    }
}
