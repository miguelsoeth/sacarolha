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

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.handlers.StringHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VendasAdapter extends ArrayAdapter<Venda> implements Filterable {

    private Context context;
    private List<Venda> originalVendas;
    private ClienteDAO clienteDAO;
    private List<Venda> filteredVendas;
    private Cliente cliente;

    @Override
    public int getCount() {
        return filteredVendas.size();
    }

    @Override
    public Venda getItem(int position) {
        return filteredVendas.get(position);
    }

    // Constructor
    public VendasAdapter(Context context, List<Venda> vendas) {
        super(context, 0, vendas);
        this.context = context;
        this.originalVendas = new ArrayList<>(vendas);
        this.filteredVendas = vendas;
        Collections.reverse(this.originalVendas);
        this.clienteDAO = new ClienteDAO(getContext());
    }

    // Inflating the layout for each row and binding data
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_venda, parent, false);
        }

        // Get the Venda object for this position
        Venda venda = getItem(position);

        TextView text_cliente_nome = convertView.findViewById(R.id.text_cliente_nome);
        TextView text_data = convertView.findViewById(R.id.text_data);
        TextView text_preco = convertView.findViewById(R.id.text_preco);

        cliente = clienteDAO.getClienteForVenda(venda);

        text_cliente_nome.setText(cliente.getNome());
        text_data.setText(StringHandler.convertToDefaultDate(venda.getData()));
        text_preco.setText(MaskHandler.applyPriceMask(String.valueOf(venda.getTotal())));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cliente = clienteDAO.getClienteForVenda(venda);
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showReviewSaleDialog(context, cliente, venda);
            }
        });

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Venda> filteredList = new ArrayList<>();

                String[] filterParts = constraint.toString().split(Shared.FILTER_SEPARATOR);
                String filterName = !filterParts[0].equals(Shared.FILTER_NULL) ? filterParts[0].toLowerCase().trim() : "";
                String filterData = !filterParts[1].equals(Shared.FILTER_NULL) ? filterParts[1].toLowerCase().trim() : "";

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalVendas);
                } else {
                    for (Venda venda : originalVendas) {
                        cliente = clienteDAO.getClienteForVenda(venda);
                        boolean conditionName = filterName.equals("null") || cliente.getNome().toLowerCase().contains(filterName);
                        boolean conditionData = filterData.equals("null") || StringHandler.convertToDefaultShortDate(venda.getData()).contains(filterData);
                        if (conditionName && conditionData) {
                            filteredList.add(venda);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredVendas.clear();
                filteredVendas.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}

