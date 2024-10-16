package com.example.sacarolha.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.util.adapters.ClienteAdapter;
import com.example.sacarolha.util.handlers.DialogHandler;

import java.util.List;

public class ClientesFragment extends Fragment {

    Button btnCadastrar, btnFiltro;
    ListView listView;
    ClienteDAO clienteDAO;
    String filtroString = null;

    public ClientesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastrarClienteFragment cadastrarClienteFragment = new CadastrarClienteFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, cadastrarClienteFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        clienteDAO = new ClienteDAO(getActivity());
        List<Cliente> clientes = clienteDAO.selectAll();
        ClienteAdapter adapter = new ClienteAdapter(getActivity(), clientes, getFragmentManager());

        listView = view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        btnFiltro = view.findViewById(R.id.btnFiltro);
        btnFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showClientFiltersDialog(getContext(), filtroString, new DialogHandler.getFilterListener() {
                    @Override
                    public void onFilterSelected(String filter, int quantity) {
                        btnFiltro.setText(quantity != 0 ? "Filtros(" + quantity + ")" : "Filtros");
                        filtroString = filter;
                        adapter.getFilter().filter(filter);
                    }
                });
            }
        });

        return view;
    }
}