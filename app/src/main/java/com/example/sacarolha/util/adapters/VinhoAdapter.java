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

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sacarolha.EditarClienteFragment;
import com.example.sacarolha.R;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.handlers.MaskHandler;

import java.util.ArrayList;
import java.util.List;

public class VinhoAdapter extends ArrayAdapter<Vinho> implements Filterable {
    private List<Vinho> originalVinhos;
    private List<Vinho> filteredVinhos;
    private FragmentManager fragmentManager;

    public VinhoAdapter(Context context, List<Vinho> vinhos, FragmentManager fragmentManager) {
        super(context, 0, vinhos);
        this.originalVinhos = new ArrayList<>(vinhos);
        this.filteredVinhos = vinhos;
        this.fragmentManager = fragmentManager;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vinho_item, parent, false);
        }

        TextView text1 = convertView.findViewById(R.id.text_vinho_nome);
        TextView text2 = convertView.findViewById(R.id.text_vinho_tipo);
        TextView text3 = convertView.findViewById(R.id.text_vinho_preco);
        Button btnItem = convertView.findViewById(R.id.btnItem);
        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vinhoId = vinho.getId();

                EditarClienteFragment editarClienteFragment = EditarClienteFragment.newInstance(vinhoId);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, editarClienteFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        text1.setText(vinho.getNome());
        text2.setText(vinho.getTipo());
        String precoString = MaskHandler.applyPriceMask(String.valueOf(vinho.getPreco()));
        text3.setText(precoString);

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Vinho> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalVinhos);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Vinho vinho : originalVinhos) {
                        if (vinho.getNome().toLowerCase().contains(filterPattern)) {
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