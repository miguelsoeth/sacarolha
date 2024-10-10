package com.example.sacarolha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.sacarolha.database.dao.VendaDAO;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.util.adapters.VendasAdapter;

import java.util.List;

public class VendasFragment extends Fragment {

    private Button btnNovaVenda;
    private ListView listView;

    public VendasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendas, container, false);

        btnNovaVenda = view.findViewById(R.id.btnNovaVenda);
        btnNovaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarrinhoFragment carrinhoFragment = new CarrinhoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, carrinhoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        listView = view.findViewById(R.id.listView);
        VendaDAO vendaDAO = new VendaDAO(getContext());
        List<Venda> vendasList = vendaDAO.selectAll();
        VendasAdapter adapter = new VendasAdapter(getContext(), vendasList);
        listView.setAdapter(adapter);

        return view;
    }
}