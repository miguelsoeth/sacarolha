package com.example.sacarolha.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.handlers.DialogHandler;
import com.example.sacarolha.util.adapters.VendaItemAdapter;

import java.util.List;

public class AdicionarItemFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    Button btnFiltro;
    ListView listView;
    VinhoDAO vinhoDAO;
    String filtroString;


    // TODO: Rename and change types of parameters
    private String mParam1;

    public AdicionarItemFragment() {
        // Required empty public constructor
    }


    public static AdicionarItemFragment newInstance(String param1) {
        AdicionarItemFragment fragment = new AdicionarItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adicionar_item, container, false);

        vinhoDAO = new VinhoDAO(getActivity());
        List<Vinho> vinhos = vinhoDAO.selectAll();
        VendaItemAdapter adapter = new VendaItemAdapter(getActivity(), vinhos, getFragmentManager());

        listView = view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        Button btnVoltar = view.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnFiltro = view.findViewById(R.id.btnFiltro);
        btnFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.showVinhosFiltersDialog(getContext(), filtroString, new DialogHandler.getFilterListener() {
                    @Override
                    public void onFilterSelected(String filter, int quantity) {
                        btnFiltro.setText(quantity != 0 ? String.format(getString(R.string.filtros_value), quantity) : getString(R.string.filtros));
                        filtroString = filter;
                        adapter.getFilter().filter(filter);
                    }
                });
            }
        });

        return view;
    }
}