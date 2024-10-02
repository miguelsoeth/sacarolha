package com.example.sacarolha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.adapters.ClienteAdapter;
import com.example.sacarolha.util.adapters.VinhoAdapter;
import com.example.sacarolha.util.enums.TiposVinhoEnum;
import com.example.sacarolha.util.handlers.SpinnerHandler;

import java.util.List;

public class VinhosFragment extends Fragment {

    Button btnCadastrar;
    ListView listView;
    VinhoDAO vinhoDAO;
    EditText searchText;
    Spinner filtrarTipo;

    String filterName = null, filterType = null;

    public VinhosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vinhos, container, false);

        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastrarVinhoFragment cadastrarVinhoFragment = new CadastrarVinhoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, cadastrarVinhoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        vinhoDAO = new VinhoDAO(getActivity());
        List<Vinho> vinhos = vinhoDAO.selectAll();
        VinhoAdapter adapter = new VinhoAdapter(getActivity(), vinhos, getFragmentManager());

        listView = view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        searchText = view.findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterName = String.valueOf(charSequence);
                adapter.getFilter().filter(filterName+";"+filterType);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        filtrarTipo = view.findViewById(R.id.filtrarTipo);
        SpinnerHandler spinner = new SpinnerHandler();
        spinner.configureSpinnerWithEnum_light(filtrarTipo, TiposVinhoEnum.class, getContext());

        filtrarTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    filterType = filtrarTipo.getSelectedItem().toString();
                    adapter.getFilter().filter(filterName+";"+filterType);
                }
                else {
                    filterType = null;
                    adapter.getFilter().filter(filterName+";"+filterType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}