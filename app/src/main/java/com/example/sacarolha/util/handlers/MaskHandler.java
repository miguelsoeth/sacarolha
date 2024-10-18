package com.example.sacarolha.util.handlers;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class MaskHandler {

    private boolean isUpdating = false;
    private String lastText = "";

    public void MaskTelefone(EditText textField) {
        textField.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (isUpdating) return;

                String unmaskedText = charSequence.toString().replaceAll("[^\\d]", "");
                String formattedText = applyPhoneMask(unmaskedText);

                if (!formattedText.equals(charSequence.toString())) {
                    isUpdating = true;
                    textField.setText(formattedText);
                    textField.setSelection(formattedText.length());
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }


    public void MaskCPF_CNPJ(EditText textField) {
        textField.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (isUpdating) return;

                String unmaskedText = charSequence.toString().replaceAll("[^\\d]", ""); // Remove non-numeric characters

                if (unmaskedText.length() > 14) return;

                String maskedText;
                if (unmaskedText.length() <= 11) {
                    maskedText = applyCpfMask(unmaskedText);
                } else {
                    maskedText = applyCnpjMask(unmaskedText);
                }

                if (!maskedText.equals(charSequence.toString())) {
                    isUpdating = true;
                    textField.setText(maskedText);
                    textField.setSelection(maskedText.length());
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }


    public void MaskPrice(EditText textField) {

        textField.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    textField.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[^\\d]", ""); // Everything except the last two digits

                    if (!cleanString.isEmpty()) {
                        double parsed = Double.parseDouble(cleanString);
                        String formatted = applyPriceMask(parsed);

                        current = formatted;
                        textField.setText(formatted);

                        // Ensure the text has content before setting selection
                        if (formatted.length() > 0) {
                            textField.setSelection(formatted.length());
                        }
                    }

                    if (cleanString.equals("00")) {
                        textField.setText("");
                    }

                    textField.addTextChangedListener(this);
                }
            }
        });
    }

    public static String removePunctuation(CharSequence input) {
        if (input == null) {
            return null;
        }
        return input.toString().replaceAll("[^0-9]", "");
    }

    public static String applyDocumentMask(String text) {
        text = removePunctuation(text);

        if (text.length() == 11) {
            text = applyCpfMask(text);
        }
        else if (text.length() == 14){
            text = applyCnpjMask(text);
        }

        return text;
    }

    private static String applyCpfMask(String text) {
        StringBuilder masked = new StringBuilder(text);
        if (text.length() > 3) {
            masked.insert(3, ".");
        }
        if (text.length() > 6) {
            masked.insert(7, ".");
        }
        if (text.length() > 9) {
            masked.insert(11, "-");
        }
        return masked.toString();
    }

    private static String applyCnpjMask(String text) {
        StringBuilder masked = new StringBuilder(text);
        if (text.length() > 2) {
            masked.insert(2, ".");
        }
        if (text.length() > 5) {
            masked.insert(6, ".");
        }
        if (text.length() > 8) {
            masked.insert(10, "/");
        }
        if (text.length() > 12) {
            masked.insert(15, "-");
        }
        return masked.toString();
    }

    public static String applyPhoneMask(String text) {
        StringBuilder masked = new StringBuilder(text);

        if (text.length() > 2) {
            masked.insert(0, "(");
            masked.insert(3, ") ");
        }

        if (text.length() > 6) {
            if (text.length() == 11) {
                masked.insert(10, "-");
            } else {
                masked.insert(9, "-");
            }
        } else if (text.length() > 4) {
            masked.insert(6, "-");
        }

        return masked.toString();
    }

    public static String applyPriceMask(String text) {
        double parsed = Double.parseDouble(text);
        String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(parsed);
        return formatted;
    }

    private static String applyPriceMask(Double value) {
        String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value / 100);
        return formatted;
    }

    public static Double getPriceValue(String input) {
        input = input.replaceAll("[^0-9]", "");
        Double parsedValue = Double.parseDouble(input);
        return (parsedValue/100);
    }
}
