package com.example.sacarolha.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.sacarolha.R;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.handlers.CarrinhoHandler;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.model.Carrinho;
import com.example.sacarolha.util.handlers.MaskHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VendaItemAdapter extends ArrayAdapter<Vinho> implements Filterable {
    private FragmentManager fragmentManager;
    private List<Vinho> originalVinhos;
    private List<Vinho> filteredVinhos;

    private CarrinhoHandler carrinhoHandler;
    List<Carrinho> carrinho;

    public VendaItemAdapter(Context context, List<Vinho> vinhos, FragmentManager fragmentManager) {
        super(context, 0, vinhos);
        this.fragmentManager = fragmentManager;
        carrinhoHandler = new CarrinhoHandler(getContext());
        carrinho = carrinhoHandler.LerCarrinho();
        this.filteredVinhos = vinhos;
        this.originalVinhos = new ArrayList<>(vinhos);
    }

    @Override
    public int getCount() {
        return filteredVinhos.size();
    }

    @Override
    public Vinho getItem(int position) {
        return filteredVinhos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Vinho vinho = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sale_item, parent, false);
        }

        TextView text_vinho_nome = convertView.findViewById(R.id.text_vinho_nome);
        TextView text_vinho_tipo = convertView.findViewById(R.id.text_vinho_tipo);
        TextView text_vinho_preco = convertView.findViewById(R.id.text_vinho_preco);
        TextView text_vinho_safra = convertView.findViewById(R.id.text_vinho_safra);


        text_vinho_nome.setText(vinho.getNome());
        text_vinho_tipo.setText(vinho.getTipo());
        String price = String.valueOf(vinho.getPreco());
        String maskedPrice = MaskHandler.applyPriceMask(getContext(), price);
        text_vinho_preco.setText(maskedPrice);
        if (vinho.getSafra() > 0) {
            text_vinho_safra.setText(getContext().getString(R.string.safra_data, vinho.getSafra()));
        }
        else {
            text_vinho_safra.setText(getContext().getString(R.string.nao_safrado));
        }

        Carrinho existingItem = carrinho.stream()
                .filter(o -> Objects.equals(o.getId(), vinho.getId()))
                .findFirst()
                .orElse(null);

        boolean alreadyInCart = existingItem != null;



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHandler dialogHandler = new DialogHandler();
                if (alreadyInCart) {
                    Toast.makeText(getContext(), getContext().getString(R.string.produto_ja_adicionado_ao_carrinho), Toast.LENGTH_SHORT).show();
                    dialogHandler.showQuantitySelectorDialog(getContext(), existingItem, new DialogHandler.EditCartListener() {
                        @Override
                        public void onItemEdited(Carrinho item) {
                            carrinho.removeIf(o -> Objects.equals(o.getId(), vinho.getId()));
                            carrinho.add(item);
                            carrinhoHandler.SalvarCarrinho(carrinho);
                            fragmentManager.popBackStack();
                        }
                    });
                }
                else {
                    dialogHandler.showQuantitySelectorDialog(getContext(), vinho, new DialogHandler.QuantitySelectorListener() {
                        @Override
                        public void onQuantitySelected(Vinho vinho, int quantity) {
                            Carrinho item = new Carrinho(vinho, quantity);
                            carrinho.add(item);
                            carrinhoHandler.SalvarCarrinho(carrinho);
                            fragmentManager.popBackStack();
                        }
                    });
                }
            }
        });


        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Vinho> filteredList = new ArrayList<>();

                String[] filterParts = constraint.toString().split(Shared.FILTER_SEPARATOR);
                String filterName = !filterParts[0].equals(Shared.FILTER_NULL) ? filterParts[0].toLowerCase().trim() : "";
                String filterType = !filterParts[1].equals(Shared.FILTER_NULL) ? filterParts[1].toLowerCase().trim() : "";

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalVinhos);
                } else {
                    for (Vinho vinho : originalVinhos) {
                        boolean conditionName = filterName.equals("null") || vinho.getNome().toLowerCase().contains(filterName);
                        boolean conditionType = filterType.equals("null") || vinho.getTipo().toLowerCase().contains(filterType);
                        if (conditionName && conditionType) {
                            filteredList.add(vinho);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredVinhos.clear();
                filteredVinhos.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
