package com.example.sacarolha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sacarolha.util.enums.EstadosEnum;
import com.example.sacarolha.util.handlers.MaskHandler;

public class CadastrarClienteFragment extends Fragment {

    private EditText editDocumento, editTelefone;
    private Spinner spinnerEstado;

    public CadastrarClienteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastrar_cliente, container, false);

        editDocumento = view.findViewById(R.id.editDocumento);
        editTelefone = view.findViewById(R.id.editTelefone);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);

        ArrayAdapter<EstadosEnum> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_rounded, EstadosEnum.values());
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.set

        // Create MaskHandler instance and apply masks
        MaskHandler handler = new MaskHandler();
        handler.MaskTelefone(editTelefone);
        handler.MaskCPF_CNPJ(editDocumento);

        return view;
    }
}