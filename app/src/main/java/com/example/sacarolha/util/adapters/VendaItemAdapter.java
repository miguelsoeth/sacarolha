package com.example.sacarolha.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.sacarolha.R;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.model.SaleItem;
import com.example.sacarolha.util.handlers.MaskHandler;

import java.util.ArrayList;
import java.util.List;

public class VendaItemAdapter extends ArrayAdapter<Vinho> {
    private FragmentManager fragmentManager;

    public VendaItemAdapter(Context context, List<Vinho> vinhos, FragmentManager fragmentManager) {
        super(context, 0, vinhos);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Vinho vinho = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sale_item, parent, false);
        }

        TextView text_vinho_nome = convertView.findViewById(R.id.text_vinho_nome);
        TextView text_vinho_tipo = convertView.findViewById(R.id.text_vinho_tipo);
        TextView text_vinho_preco = convertView.findViewById(R.id.text_vinho_preco);
        TextView text_vinho_safra = convertView.findViewById(R.id.text_vinho_safra);

        EditText quantItem = convertView.findViewById(R.id.quantItem);
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

        Button quantMinus = convertView.findViewById(R.id.quantMinus);
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

        Button quantPlus = convertView.findViewById(R.id.quantPlus);
        quantPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = quantItem.getText().toString().trim();
                if (input.isEmpty()) {
                    quantItem.setText("0");
                }
                else {
                    int quantity = Integer.parseInt(quantItem.getText().toString());
                    quantItem.setText(String.valueOf(quantity + 1));
                }
            }
        });

        convertView.setOnTouchListener(new View.OnTouchListener() {
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


        return convertView;
    }

    public List<SaleItem> getSelectedVinhos(ListView listView) {
        List<SaleItem> selectedVinhos = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            Vinho vinho = getItem(i);
            View itemView = listView.getChildAt(i); // Get the view for this position
            if (itemView != null) {
                EditText quantItem = itemView.findViewById(R.id.quantItem);
                int quantity = Integer.parseInt(quantItem.getText().toString().trim());
                if (quantity > 0) {
                    SaleItem saleItem = new SaleItem();
                    saleItem.setId(vinho.getId());
                    saleItem.setNome(vinho.getNome());
                    saleItem.setPreco(vinho.getPreco());
                    saleItem.setQuantity(quantity);
                    selectedVinhos.add(saleItem);
                }
            }
        }
        return selectedVinhos;
    }
}
