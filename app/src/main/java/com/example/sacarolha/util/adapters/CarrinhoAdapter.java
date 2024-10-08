package com.example.sacarolha.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sacarolha.R;
import com.example.sacarolha.util.handlers.CarrinhoHandler;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.model.Carrinho;

import java.util.List;

public class CarrinhoAdapter extends ArrayAdapter<Carrinho> {

    private CarrinhoHandler carrinhoHandler;

    public CarrinhoAdapter(Context context, List<Carrinho> carrinho) {
        super(context, 0, carrinho);
        carrinhoHandler = new CarrinhoHandler(getContext());
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

        String maskedPriceUnt = MaskHandler.applyPriceMask(String.valueOf(item.getPreco()));
        text_vinho_preco_unt.setText(maskedPriceUnt);

        Double total = item.getPreco() * item.getQuantidade();
        String maskedPriceTotal = MaskHandler.applyPriceMask(String.valueOf(total));
        text_vinho_preco_total.setText(maskedPriceTotal);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showItemDetailsDialog(getContext(), item, new DialogHandler.ItemDetailsListener() {
                    @Override
                    public void onEditedItem(Carrinho editedItem) {
                        text_vinho_nome.setText(editedItem.getNome());
                        text_vinho_quantidade.setText(String.valueOf(editedItem.getQuantidade()));

                        String maskedPriceUnt = MaskHandler.applyPriceMask(String.valueOf(editedItem.getPreco()));
                        text_vinho_preco_unt.setText(maskedPriceUnt);

                        Double total = editedItem.getPreco() * editedItem.getQuantidade();
                        String maskedPriceTotal = MaskHandler.applyPriceMask(String.valueOf(total));
                        text_vinho_preco_total.setText(maskedPriceTotal);

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onDeletedItem() {
                        carrinhoHandler.RemoverDoCarrinho(item);
                        remove(item);

                    }
                });
            }
        });

        return convertView;
    }
}
