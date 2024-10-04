package com.example.sacarolha;

import android.app.AlertDialog;
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

import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.enums.EstadosEnum;
import com.example.sacarolha.util.enums.TiposVinhoEnum;
import com.example.sacarolha.util.handlers.AlertHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.handlers.SpinnerHandler;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class EditarVinhoFragment extends Fragment {

    private static final String ARG_VINHO_ID = "vinhoId";
    private EditText editNome, editSafra, editPreco, editEstoque, editCodigo;
    private Spinner spinnerTipo;
    private Button btnSalvar, btnScanCodigo, btnVoltar;
    boolean seedingUpdate = true;

    private String mVinhoId;

    public EditarVinhoFragment() {
        // Required empty public constructor
    }

    public static EditarVinhoFragment newInstance(String vinhoId) {
        EditarVinhoFragment fragment = new EditarVinhoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VINHO_ID, vinhoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVinhoId = getArguments().getString(ARG_VINHO_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_vinho, container, false);

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

        VinhoDAO vinhoDAO = new VinhoDAO(getContext());
        Vinho v = vinhoDAO.selectById(mVinhoId);

        if (v == null) {
            Toast.makeText(getContext(), "Vinho não encontrado!", Toast.LENGTH_SHORT).show();
        }
        else {
            Integer pos = TiposVinhoEnum.getPosition(v.getTipo());
            seedingUpdate = true;
            spinnerTipo.setSelection(pos);

            editNome.setText(v.getNome());
            Integer safra = v.getSafra();
            if (!safra.equals(0)) {
                editSafra.setText(String.valueOf(safra));
            }
            editEstoque.setText(String.valueOf(v.getEstoque()));
            editPreco.setText(MaskHandler.applyPriceMask(String.valueOf(v.getPreco())));
            editCodigo.setText(v.getCodigo());

        }

        btnSalvar = view.findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidFields()) {

                    AlertHandler.showSimpleAlert(getContext(), "Salvar alterações?", "", "Sim", new AlertHandler.AlertCallback() {
                        @Override
                        public void onPositiveButtonClicked() {
                            insertValues();
                        }

                        @Override
                        public void onNegativeButtonClicked() {

                        }
                    });
                }
            }
        });

        btnVoltar = view.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });


        btnScanCodigo = view.findViewById(R.id.btnScanCodigo);
        btnScanCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCode();
            }
        });

        MaskHandler handler = new MaskHandler();
        handler.MaskPrice(editPreco);

        return view;
    }

    private void insertValues() {
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
        String tipo = spinnerTipo.getSelectedItem().toString();
        String codigo = editCodigo.getText().toString().trim();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userId = preferences.getString(Shared.KEY_USER_ID, "");

        preco = MaskHandler.removePunctuation(preco);
        preco = preco.substring(0, preco.length() - 2) + "." + preco.substring(preco.length() - 2);
        Double parsedPreco = Double.parseDouble(preco);

        Vinho vinho = new Vinho(mVinhoId, nome, tipo, safra, parsedPreco, estoque, codigo, userId);

        VinhoDAO vinhoDAO = new VinhoDAO(getContext());
        long result = vinhoDAO.update(vinho);

        if (result > 0) {
            Toast.makeText(getContext(), "Vinho salvo com sucesso!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
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
        Vinho v = vinhoDAO.selectByCodigoWithId(codigo, mVinhoId);
        if (v != null && !codigo.isEmpty()) {
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