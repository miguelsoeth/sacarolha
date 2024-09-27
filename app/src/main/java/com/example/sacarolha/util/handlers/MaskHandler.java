package com.example.sacarolha.util.handlers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskHandler {

    private boolean isUpdating = false;
    private String lastText = "";

    public void MaskTelefone(EditText textField) {

        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String unmaskedText = charSequence.toString().replaceAll("[^\\d]", "");

                String formattedText = applyPhoneMask(unmaskedText);

                isUpdating = true;
                textField.setText(formattedText);
                textField.setSelection(formattedText.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    public void MaskCPF_CNPJ(EditText textField) {
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String unmaskedText = charSequence.toString().replaceAll("[^\\d]", "");
                String maskedText;
                if (unmaskedText.length() <= 11) {
                    maskedText = applyCpfMask(unmaskedText);
                } else {
                    maskedText = applyCnpjMask(unmaskedText);
                }
                lastText = maskedText;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String unmaskedText = charSequence.toString().replaceAll("[^\\d]", "");  // Remove non-numeric characters

                if (unmaskedText.length() > 14) {
                    return;
                }

                String maskedText = "";

                if (unmaskedText.length() <= 11) {
                    maskedText = applyCpfMask(unmaskedText);
                } else if (unmaskedText.length() <= 14) {
                    maskedText = applyCnpjMask(unmaskedText);
                }

                isUpdating = true;
                textField.setText(maskedText);
                lastText = maskedText;
                textField.setSelection(maskedText.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public static String removePunctuation(CharSequence input) {
        if (input == null) {
            return null;
        }
        return input.toString().replaceAll("[^0-9]", "");
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

    private static String applyPhoneMask(String text) {
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
}
