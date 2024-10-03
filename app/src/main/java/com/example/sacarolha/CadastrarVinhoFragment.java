package com.example.sacarolha;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.enums.TiposVinhoEnum;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.handlers.SpinnerHandler;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class CadastrarVinhoFragment extends Fragment {

    private EditText editNome, editSafra, editPreco, editEstoque, editCodigo;
    private Spinner spinnerTipo;
    private Button btnCadastrar, btnScanCodigo;

    public CadastrarVinhoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastrar_vinho, container, false);
        editSafra = view.findViewById(R.id.editSafra);
        editEstoque = view.findViewById(R.id.editEstoque);
        editNome = view.findViewById(R.id.editNome);
        editCodigo = view.findViewById(R.id.editCodigo);

        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        SpinnerHandler spinner = new SpinnerHandler();
        spinner.configureSpinnerWithEnum(spinnerTipo, TiposVinhoEnum.class, getContext());

        MaskHandler mask = new MaskHandler();
        editPreco = view.findViewById(R.id.editPreco);
        mask.MaskPrice(editPreco);

        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidFields()) {

                    String nome = editNome.getText().toString().trim();
                    Integer safra;
                    if ( editSafra.getText() != null && !editSafra.getText().toString().isEmpty() ) {
                        safra = Integer.parseInt(editSafra.getText().toString().trim());
                    }
                    else {
                        safra = 0;
                    }
                    String preco = editPreco.getText().toString().trim();
                    Integer estoque = Integer.parseInt(editEstoque.getText().toString().trim());
                    String codigo = editCodigo.getText().toString().trim();
                    String tipo = spinnerTipo.getSelectedItem().toString();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String userId = preferences.getString(Shared.KEY_USER_ID, "");

                    preco = MaskHandler.removePunctuation(preco);
                    preco = preco.substring(0, preco.length() - 2) + "." + preco.substring(preco.length() - 2);
                    Double parsedPreco = Double.parseDouble(preco);

                    Vinho vinho = new Vinho(nome, tipo, safra, parsedPreco, estoque, codigo, userId);

                    VinhoDAO vinhoDAO = new VinhoDAO(getContext());
                    long result = vinhoDAO.insert(vinho);

                    if (result > 0) {
                        Toast.makeText(getContext(), "Vinho cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            }
        });

        btnScanCodigo = view.findViewById(R.id.btnScanCodigo);
        btnScanCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCode();
            }
        });

        return view;
    }

    private boolean ValidFields() {
        editNome.setError(null);
        ((TextView)spinnerTipo.getSelectedView()).setError(null);
        editEstoque.setError(null);
        editPreco.setError(null);
        editCodigo.setError(null);

        boolean isOkay = true;

        // Get text from EditTexts
        String nome = editNome.getText().toString().trim();
        String preco = editPreco.getText().toString().trim();
        String estoque = editEstoque.getText().toString().trim();
        String codigo = editCodigo.getText().toString().trim();

        // Validate each field and set error messages
        if (nome.isEmpty()) {
            editNome.setError("Nome não pode estar vazio!");
            isOkay = false;
        }

        if (preco.isEmpty()) {
            editPreco.setError("Preço não pode estar vazio!");
            isOkay = false;
        }

        if (estoque.isEmpty()) {
            editEstoque.setError("Estoque não pode estar vazio!");
            isOkay = false;
        }

        if (spinnerTipo.getSelectedItem() == null || spinnerTipo.getSelectedItemPosition() == 0) {
            ((TextView)spinnerTipo.getSelectedView()).setError("Selecione um tipo!");
            isOkay = false;
        }

        VinhoDAO vinhoDAO = new VinhoDAO(getContext());
        Vinho v = vinhoDAO.selectByCodigo(codigo);
        if (v != null) {
            editCodigo.setError("Já existe vinho com este código!");
            isOkay = false;
        }

        return isOkay;
    }

    private void ScanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Aumentar/Diminuir o volume para Ligar/Desligar o flash");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            editCodigo.setError(null);
            editCodigo.setText(result.getContents());
        }
    });

}