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
import androidx.fragment.app.FragmentTransaction;

import com.example.sacarolha.CadastrarClienteFragment;
import com.example.sacarolha.EditarClienteFragment;
import com.example.sacarolha.R;
import com.example.sacarolha.database.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteAdapter extends ArrayAdapter<Cliente> implements Filterable {
    private List<Cliente> originalClientes;
    private List<Cliente> filteredClientes;
    private FragmentManager fragmentManager;

    public ClienteAdapter(Context context, List<Cliente> clientes, FragmentManager fragmentManager) {
        super(context, 0, clientes);
        this.originalClientes = new ArrayList<>(clientes);
        this.filteredClientes = clientes;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return filteredClientes.size();
    }

    @Override
    public Cliente getItem(int position) {
        return filteredClientes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cliente cliente = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cliente_item, parent, false);
        }

        TextView text1 = convertView.findViewById(R.id.text_cliente_nome);
        TextView text2 = convertView.findViewById(R.id.text_cliente_documento);
        Button btnItem = convertView.findViewById(R.id.btnItem);
        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String clienteId = cliente.getId();

                EditarClienteFragment editarClienteFragment = EditarClienteFragment.newInstance(clienteId);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, editarClienteFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        text1.setText(cliente.getNome());
        text2.setText(cliente.getDocumento());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Cliente> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalClientes);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Cliente cliente : originalClientes) {
                        if (cliente.getNome().toLowerCase().contains(filterPattern)) {
                            filteredList.add(cliente);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredClientes.clear();
                filteredClientes.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}