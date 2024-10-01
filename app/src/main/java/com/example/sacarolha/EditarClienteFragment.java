package com.example.sacarolha;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.enums.EstadosEnum;
import com.example.sacarolha.util.handlers.DocumentHandler;
import com.example.sacarolha.util.handlers.MaskHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditarClienteFragment extends Fragment {
    
    private static final String ARG_CLIENTE_ID = "clienteId";
    private EditText editNome, editMail, editDocumento, editTelefone, editCidade, editRua, editBairro, editNumero, editComplemento;
    private Spinner spinnerEstado;
    Button btnSalvar;
    boolean seedingUpdate = true;

    // TODO: Rename and change types of parameters
    private String mClienteId;

    public EditarClienteFragment() {
        // Required empty public constructor
    }

    public static EditarClienteFragment newInstance(String clienteId) {
        EditarClienteFragment fragment = new EditarClienteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLIENTE_ID, clienteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClienteId = getArguments().getString(ARG_CLIENTE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_cliente, container, false);

        editDocumento = view.findViewById(R.id.editDocumento);
        editNome = view.findViewById(R.id.editNome);
        editMail = view.findViewById(R.id.editMail);
        editTelefone = view.findViewById(R.id.editTelefone);
        editCidade = view.findViewById(R.id.editCidade);
        editRua = view.findViewById(R.id.editRua);
        editBairro = view.findViewById(R.id.editBairro);
        editNumero = view.findViewById(R.id.editNumero);
        editComplemento = view.findViewById(R.id.editComplemento);

        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        configureSpinnerWithEnum(spinnerEstado, EstadosEnum.class);

        ClienteDAO clienteDAO = new ClienteDAO(getContext());
        Cliente c = clienteDAO.selectById(mClienteId);

        if (c == null) {
            Toast.makeText(getContext(), "Cliente não encontrado!", Toast.LENGTH_SHORT).show();
        }
        else {
            Integer pos = EstadosEnum.getPosition(c.getEstado());
            seedingUpdate = true;
            spinnerEstado.setSelection(pos);

            editDocumento.setText(MaskHandler.applyDocumentMask(c.getDocumento()));
            editNome.setText(c.getNome());
            editMail.setText(c.getEmail());
            editTelefone.setText(MaskHandler.applyPhoneMask(c.getTelefone()));
            editCidade.setText(c.getCidade());
            editRua.setText(c.getRua());
            editBairro.setText(c.getBairro());
            editNumero.setText(c.getNumero());
            editComplemento.setText(c.getComplemento());

        }

        btnSalvar = view.findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidFields()) {

                    String documento = editDocumento.getText().toString().trim();
                    String nome = editNome.getText().toString().trim();
                    String email = editMail.getText().toString().trim();
                    String telefone = editTelefone.getText().toString().trim();
                    String cidade = editCidade.getText().toString().trim();
                    String rua = editRua.getText().toString().trim();
                    String bairro = editBairro.getText().toString().trim();
                    String numero = editNumero.getText().toString().trim();
                    String complemento = editComplemento.getText().toString().trim();
                    String estado = spinnerEstado.getSelectedItem().toString();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String userId = preferences.getString(Shared.KEY_USER_ID, "");

                    documento = MaskHandler.removePunctuation(documento);
                    telefone = MaskHandler.removePunctuation(telefone);

                    Cliente cliente = new Cliente(mClienteId, nome, documento, telefone, rua, bairro, complemento, numero, cidade, estado, email, userId);

                    ClienteDAO clienteDAO = new ClienteDAO(getContext());
                    long result = clienteDAO.update(cliente);

                    if (result > 0) {
                        Toast.makeText(getContext(), "Cliente salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            }
        });

        MaskHandler handler = new MaskHandler();
        handler.MaskTelefone(editTelefone);
        handler.MaskCPF_CNPJ(editDocumento);

        return view;

    }
    private boolean ValidFields() {
        // Clear previous errors
        editDocumento.setError(null);
        editNome.setError(null);
        editTelefone.setError(null);


        boolean isOkay = true;

        // Get text from EditTexts
        String documento = editDocumento.getText().toString().trim();
        String nome = editNome.getText().toString().trim();
        String telefone = editTelefone.getText().toString().trim();
        String email = editMail.getText().toString().trim();

        // Validate each field and set error messages
        if (documento.isEmpty()) {
            editDocumento.setError("Documento não pode estar vazio!");
            isOkay = false;
        }
        else if(!DocumentHandler.isValidDocument(documento)) {
            editDocumento.setError("Documento inválido");
            isOkay = false;
        }

        if (nome.isEmpty()) {
            editNome.setError("Nome não pode estar vazio!");
            isOkay = false;
        }

        if (telefone.isEmpty()) {
            editTelefone.setError("Telefone não pode estar vazio!");
            isOkay = false;
        }

        if (email.isEmpty()) {
            editMail.setError("Email não pode estar vazio!");
            isOkay = false;
        }

        // Validate spinner state
        if (spinnerEstado.getSelectedItem() == null || spinnerEstado.getSelectedItemPosition() == 0) {
            ((TextView)spinnerEstado.getSelectedView()).setError("Selecione um estado!");
            isOkay = false;
        }

        return isOkay;
    }

    private <E extends Enum<E>> void configureSpinnerWithEnum(Spinner spinner, Class<E> enumClass) {

        List<E> enumList = new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
        ArrayAdapter<E> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_rounded, enumList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (seedingUpdate) {
                    seedingUpdate = false; // Reset flag
                    return;
                }

                if (i == 0) {
                    ((TextView) view).setTextColor(getResources().getColor(R.color.transparent_white));
                    setInputsFontSize(0);
                    setBackgroundResource(R.drawable.bkg_medium_purple_rounded);
                    setInputsStatusNoClear(false);
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                    setInputsFontSize(20);
                    setBackgroundResource(R.drawable.bkg_dark_purple_rounded);
                    setInputsStatusNoClear(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setInputsStatus(boolean status) {
        editCidade.setText("");
        editRua.setText("");
        editBairro.setText("");
        editNumero.setText("");
        editComplemento.setText("");

        editCidade.setEnabled(status);
        editRua.setEnabled(status);
        editBairro.setEnabled(status);
        editNumero.setEnabled(status);
        editComplemento.setEnabled(status);
    }

    private void setInputsStatusNoClear(boolean status) {
        editCidade.setEnabled(status);
        editRua.setEnabled(status);
        editBairro.setEnabled(status);
        editNumero.setEnabled(status);
        editComplemento.setEnabled(status);
    }

    private void setInputsFontSize(int size) {
        editCidade.setTextSize(size);
        editRua.setTextSize(size);
        editBairro.setTextSize(size);
        editNumero.setTextSize(size);
        editComplemento.setTextSize(size);
    }

    private void setBackgroundResource(int resource) {
        editCidade.setBackgroundResource(resource);
        editRua.setBackgroundResource(resource);
        editBairro.setBackgroundResource(resource);
        editNumero.setBackgroundResource(resource);
        editComplemento.setBackgroundResource(resource);
    }
}