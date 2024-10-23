package com.example.sacarolha.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacarolha.CaptureActivity;
import com.example.sacarolha.R;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.enums.TiposVinhoEnum;
import com.example.sacarolha.util.handlers.BarcodeHandler;
import com.example.sacarolha.util.handlers.CodeHandler;
import com.example.sacarolha.util.handlers.ImageHandler;
import com.example.sacarolha.util.handlers.MaskHandler;
import com.example.sacarolha.util.handlers.SpinnerHandler;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;

public class CadastrarVinhoFragment extends Fragment {

    private EditText editNome, editSafra, editPreco, editEstoque, editCodigo;
    private Spinner spinnerTipo;
    private Button btnCadastrar, btnScanCodigo, btnVoltar, btnCompartilharCodigo, btnGerarCodigo;
    private Bitmap barcodeBitmap;
    private LinearLayout barcodeFull;
    private TextView txtCodigoNumeros;

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
        ImageView barcodeImageView = view.findViewById(R.id.barcodeImageView);
        editCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    barcodeImageView.setImageBitmap(null);
                    txtCodigoNumeros.setText("");
                    return;
                }

                barcodeBitmap = BarcodeHandler.generateBarcode(s.toString());
                if (barcodeBitmap != null) {
                    barcodeImageView.setImageBitmap(barcodeBitmap);
                    txtCodigoNumeros.setText(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGerarCodigo = view.findViewById(R.id.btnGerarCodigo);
        btnGerarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCodigo.setText(CodeHandler.generateUniqueNumberHash());
            }
        });

        barcodeFull = view.findViewById(R.id.barcodeFull);
        txtCodigoNumeros = view.findViewById(R.id.txtCodigoNumeros);
        btnCompartilharCodigo = view.findViewById(R.id.btnCompartilharCodigo);
        btnCompartilharCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editCodigo.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.nao_ha_codigo_de_barras_para_compartilhar), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Bitmap bitmap = ImageHandler.getBitmapFromView(barcodeFull);
                    Uri imageUri = ImageHandler.saveBitmapToFile(getContext(), bitmap);
                    ImageHandler.shareImage(getContext(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.share_image_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        SpinnerHandler spinner = new SpinnerHandler();
        spinner.configureSpinnerWithEnum(spinnerTipo, TiposVinhoEnum.class, getContext());

        MaskHandler mask = new MaskHandler();
        editPreco = view.findViewById(R.id.editPreco);
        mask.MaskPrice(editPreco);

        btnVoltar = view.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

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
                    Double parsedPreco = MaskHandler.getPriceValue(preco);

                    Vinho vinho = new Vinho(nome, tipo, safra, parsedPreco, estoque, codigo, userId);

                    VinhoDAO vinhoDAO = new VinhoDAO(getContext());
                    long result = vinhoDAO.insert(vinho);

                    if (result > 0) {
                        Toast.makeText(getContext(), getString(R.string.vinho_cadastrado_com_sucesso), Toast.LENGTH_SHORT).show();
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
        ((TextView) spinnerTipo.getSelectedView()).setError(null);
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
            editNome.setError(getString(R.string.error_nome_vazio));
            isOkay = false;
        }

        if (preco.isEmpty()) {
            editPreco.setError(getString(R.string.error_preco_vazio));
            isOkay = false;
        }

        if (estoque.isEmpty()) {
            editEstoque.setError(getString(R.string.error_estoque_vazio));
            isOkay = false;
        }

        if (spinnerTipo.getSelectedItem() == null || spinnerTipo.getSelectedItemPosition() == 0) {
            ((TextView) spinnerTipo.getSelectedView()).setError(getString(R.string.error_selecionar_tipo));
            isOkay = false;
        }

        VinhoDAO vinhoDAO = new VinhoDAO(getContext());
        Vinho v = vinhoDAO.selectByCodigo(codigo);
        if (v != null && !codigo.isEmpty()) {
            editCodigo.setError(getString(R.string.error_codigo_existente));
            isOkay = false;
        }

        return isOkay;
    }

    private void ScanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.scan_code_prompt));
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