package com.example.sacarolha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sacarolha.util.handlers.MaskHandler;

public class ClientesFragment extends Fragment {
    private EditText editDocumento, editTelefone;

    public ClientesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        editDocumento = view.findViewById(R.id.editDocumento);
        editTelefone = view.findViewById(R.id.editTelefone);

        // Create MaskHandler instance and apply masks
        MaskHandler handler = new MaskHandler();
        handler.MaskTelefone(editTelefone);
        handler.MaskCPF_CNPJ(editDocumento);

        return view;
    }
}