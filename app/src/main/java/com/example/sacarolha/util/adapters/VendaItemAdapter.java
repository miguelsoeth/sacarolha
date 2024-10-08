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
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.sacarolha.R;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.handlers.CarrinhoHandler;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.model.Carrinho;
import com.example.sacarolha.util.model.SaleItem;
import com.example.sacarolha.util.handlers.MaskHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VendaItemAdapter extends ArrayAdapter<Vinho> {
    private FragmentManager fragmentManager;

    private CarrinhoHandler carrinhoHandler;
    List<Carrinho> carrinho;

    public VendaItemAdapter(Context context, List<Vinho> vinhos, FragmentManager fragmentManager) {
        super(context, 0, vinhos);
        this.fragmentManager = fragmentManager;
        carrinhoHandler = new CarrinhoHandler(getContext());
        carrinho = carrinhoHandler.LerCarrinho();
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

        Button btnAdicionar = convertView.findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean alreadyInCart = carrinho.stream().anyMatch(o -> Objects.equals(o.getId(), vinho.getId()));

                if (alreadyInCart) {
                    Toast.makeText(getContext(), "Produto já adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
                    //Send to edit product
                }
                else {
                    DialogHandler dialogHandler = new DialogHandler();
                    dialogHandler.showQuantitySelectorDialog(getContext(), vinho, new DialogHandler.QuantitySelectorListener() {
                        @Override
                        public void onQuantitySelected(Vinho vinho, int quantity) {
                            Carrinho item = new Carrinho(vinho, quantity);
                            carrinho.add(item);
                            carrinhoHandler.SalvarCarrinho(carrinho);

//                            Double precoTotal = item.getPreco() * item.getQuantidade();
//                            Double totalValue = MaskHandler.getPriceValue(total);
//                            totalValue = totalValue + precoTotal;
//
//                            String priceTotal = String.valueOf(totalValue);
//                            String maskedPriceTotal = MaskHandler.applyPriceMask(priceTotal);
//                            carrinhoHandler.SalvarTotalCarrinho(maskedPriceTotal);

                            fragmentManager.popBackStack();

                        }
                    });
                }
            }
        });


        return convertView;
    }
}
