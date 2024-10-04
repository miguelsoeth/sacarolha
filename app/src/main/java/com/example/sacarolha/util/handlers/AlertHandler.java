package com.example.sacarolha.util.handlers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.sacarolha.R;

public class AlertHandler {

    public static final Integer TEXT_TITLE_SIZE = 28;
    public static final Integer TEXT_DESCRIPTION_SIZE = 20;
    public static final Integer TEXT_BUTTON_SIZE = 18;

    // Callback interface to handle button clicks
    public interface AlertCallback {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }

    private static TextView createCustomBoldTextView(Context context, String text, float textSize) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setPadding(0, 20, 0, 0);

        // Set custom font from res/font/zain_bold.ttf
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.zain_bold)); // Adjust padding as needed
        textView.setGravity(Gravity.CENTER);  // Optional: Center the text
        return textView;
    }

    private static TextView createCustomRegularTextView(Context context, String text, float textSize) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setPadding(0, 10, 0, 0);

        if (text.isEmpty()) {
            textView.setHeight(0);
        }

        // Set custom font from res/font/zain_bold.ttf
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.zain_regular)); // Adjust padding as needed
        textView.setGravity(Gravity.CENTER);  // Optional: Center the text
        return textView;
    }



    public static void showSimpleAlert(Context context, String title, final AlertCallback callback) {
        showSimpleAlert(context, title, "", "Ok",callback);
    }

    public static void showSimpleAlert(Context context, String title, String description, final AlertCallback callback) {
        showSimpleAlert(context, title, description, "Ok",callback);
    }

    public static void showSimpleAlert(Context context, String title, String description, String confirmText, final AlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView titleView = createCustomBoldTextView(context, title, TEXT_TITLE_SIZE);
        builder.setCustomTitle(titleView);

        if (!description.isEmpty()) {
            TextView messageView = createCustomRegularTextView(context, description, TEXT_DESCRIPTION_SIZE);
            builder.setView(messageView);
        }

        // Add an OK button
        builder.setPositiveButton(confirmText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the OK button click
                if (callback != null) {
                    callback.onPositiveButtonClicked();
                }
                dialog.dismiss();
            }
        });

        // Add a Cancel button
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the Cancel button click
                if (callback != null) {
                    callback.onNegativeButtonClicked();
                }
                dialog.dismiss();
            }
        });

        // Create and show the alert dialog
        AlertDialog alert = builder.create();
        alert.show();

        Button positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positiveButton != null) {
            Typeface font = ResourcesCompat.getFont(context, R.font.zain_bold);
            positiveButton.setTextSize(TEXT_BUTTON_SIZE);
            positiveButton.setTypeface(font);
        }

        // Set custom font for the negative button
        Button negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            Typeface font = ResourcesCompat.getFont(context, R.font.zain_regular);
            negativeButton.setTextSize(TEXT_BUTTON_SIZE);
            negativeButton.setTypeface(font);
        }
    }
}
