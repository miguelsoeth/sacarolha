package com.example.sacarolha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class VendasFragment extends Fragment {

    private Button btnNovaVenda;

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


        return view;
    }
}