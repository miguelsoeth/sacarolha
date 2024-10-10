package com.example.sacarolha.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.handlers.StringHandler;

import java.util.List;

public class VendasAdapter extends ArrayAdapter<Venda> {

    private Context context;
    private List<Venda> vendas;
    private ClienteDAO clienteDAO;

    // Constructor
    public VendasAdapter(Context context, List<Venda> vendas) {
        super(context, 0, vendas);
        this.context = context;
        this.vendas = vendas;
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

        Cliente c = clienteDAO.getClienteForVenda(venda);

        text_cliente_nome.setText(c.getNome());
        text_data.setText(StringHandler.convertToDefaultDate(venda.getData()));
        text_preco.setText(MaskHandler.applyPriceMask(String.valueOf(venda.getTotal())));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showReviewSaleDialog(context, c, venda);
            }
        });

        return convertView;
    }
}

