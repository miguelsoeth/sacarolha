package com.example.sacarolha.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.util.adapters.ClienteAdapter;
import com.example.sacarolha.util.adapters.ClienteRelatorioAdapter;
import com.example.sacarolha.util.handlers.DialogHandler;

import java.util.List;

public class SelecionarClienteFragment extends Fragment {

    private Button btnFiltrar;
    private ListView listview;
    private ImageView btnBackToCart;
    private ClienteDAO clienteDAO;
    String filtroString = null;

    public SelecionarClienteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clienteDAO = new ClienteDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selecionar_cliente, container, false);

        btnBackToCart = view.findViewById(R.id.btnBackToCart);
        btnBackToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        clienteDAO = new ClienteDAO(getActivity());
        List<Cliente> clientes = clienteDAO.selectAll();
        ClienteRelatorioAdapter adapter = new ClienteRelatorioAdapter(getActivity(), clientes, getFragmentManager());
        listview = view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showClientFiltersDialog(getContext(), filtroString, new DialogHandler.getFilterListener() {
                    @Override
                    public void onFilterSelected(String filter, int quantity) {
                        btnFiltrar.setText(quantity != 0 ? "Filtros(" + quantity + ")" : "Filtros");
                        filtroString = filter;
                        adapter.getFilter().filter(filter);
                    }
                });
            }
        });

        return view;
    }
}