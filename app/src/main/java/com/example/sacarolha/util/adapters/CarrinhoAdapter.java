package com.example.sacarolha.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sacarolha.R;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.model.Carrinho;

import java.util.List;

public class CarrinhoAdapter extends ArrayAdapter<Carrinho> {

    public CarrinhoAdapter(Context context, List<Carrinho> carrinho) {
        super(context, 0, carrinho);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Carrinho item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, parent, false);
        }

        TextView text_vinho_nome = convertView.findViewById(R.id.text_vinho_nome);
        TextView text_vinho_quantidade = convertView.findViewById(R.id.text_vinho_quantidade);
        TextView text_vinho_preco_unt = convertView.findViewById(R.id.text_vinho_preco_unt);
        TextView text_vinho_preco_total = convertView.findViewById(R.id.text_vinho_preco_total);

        text_vinho_nome.setText(item.getNome());
        text_vinho_quantidade.setText(String.valueOf(item.getQuantidade()));

        String priceUnt = String.valueOf(item.getPreco());
        String maskedPriceUnt = MaskHandler.applyPriceMask(priceUnt);
        text_vinho_preco_unt.setText(maskedPriceUnt);

        Double total = item.getPreco() * item.getQuantidade();
        String priceTotal = String.valueOf(total);
        String maskedPriceTotal = MaskHandler.applyPriceMask(priceTotal);
        text_vinho_preco_total.setText(maskedPriceTotal);

        return convertView;
    }
}
