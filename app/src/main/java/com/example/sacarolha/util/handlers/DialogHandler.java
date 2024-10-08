package com.example.sacarolha.util.handlers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sacarolha.R;
import com.example.sacarolha.database.model.Vinho;

import org.w3c.dom.Text;

public class DialogHandler {

    public interface QuantitySelectorListener {
        void onQuantitySelected(Vinho vinho, int quantity);
    }

    public void showQuantitySelectorDialog(Context context, Vinho vinho, QuantitySelectorListener listener) {
        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity_selector, null);

        dialog.setContentView(view);

        TextView text_vinho_nome = view.findViewById(R.id.text_vinho_nome);
        TextView text_vinho_tipo = view.findViewById(R.id.text_vinho_tipo);
        TextView text_vinho_preco = view.findViewById(R.id.text_vinho_preco);
        TextView text_vinho_safra = view.findViewById(R.id.text_vinho_safra);
        TextView text_vinho_estoque = view.findViewById(R.id.text_vinho_estoque);

        text_vinho_estoque.setText("Estoque: " + String.valueOf(vinho.getEstoque()));
        text_vinho_nome.setText(vinho.getNome());
        text_vinho_tipo.setText(vinho.getTipo());
        String price = String.valueOf(vinho.getPreco());
        String maskedPrice = MaskHandler.applyPriceMask(price);
        text_vinho_preco.setText(maskedPrice);
        if (vinho.getSafra() > 0) {
            text_vinho_safra.setText("Safra " + String.valueOf(vinho.getSafra()));
        }
        else {
            text_vinho_safra.setText("Não-safrado");
        }

        EditText quantItem = view.findViewById(R.id.quantItem);
        quantItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (quantItem.getText().toString().trim().isEmpty()) {
                        quantItem.setText("0");
                    }
                    else {
                        int quantity = Integer.parseInt(quantItem.getText().toString());
                        quantItem.setText(String.valueOf(quantity));
                    }
                }
            }
        });

        Button quantMinus = view.findViewById(R.id.quantMinus);
        quantMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = quantItem.getText().toString().trim();
                if (input.isEmpty()) {
                    quantItem.setText("0");
                }
                else {
                    int quantity = Integer.parseInt(quantItem.getText().toString());

                    if (quantity > 0) {
                        quantItem.setText(String.valueOf(quantity - 1));
                    }
                }
            }
        });

        Button quantPlus = view.findViewById(R.id.quantPlus);
        quantPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = quantItem.getText().toString().trim();
                if (input.isEmpty()) {
                    quantItem.setText("0");
                }
                else {
                    int quantity = Integer.parseInt(quantItem.getText().toString());
                    if (quantity < vinho.getEstoque()) {
                        quantItem.setText(String.valueOf(quantity + 1));
                    }
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (quantItem.isFocused()) {
                        quantItem.clearFocus();
                        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });


        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        Button btnAdicionar = view.findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(quantItem.getText().toString());
                if (quantity > 0) {
                    listener.onQuantitySelected(vinho, quantity);  // Trigger the callback
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

}
