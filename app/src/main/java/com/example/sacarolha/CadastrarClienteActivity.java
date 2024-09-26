package com.example.sacarolha;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sacarolha.util.enums.EstadosEnum;
import com.santalu.maskara.Mask;
import com.santalu.maskara.MaskChangedListener;
import com.santalu.maskara.MaskStyle;
import com.santalu.maskara.widget.MaskEditText;
import com.vicmikhailau.maskededittext.MaskedEditText;
import com.vicmikhailau.maskededittext.MaskedFormatter;
import com.vicmikhailau.maskededittext.MaskedWatcher;

public class CadastrarClienteActivity extends BaseActivity {


    MaskEditText editTelefone;
    EditText editDocumento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //editTelefone = findViewById(R.id.editTelefone);
        editDocumento = findViewById(R.id.editDocumento);
        editDocumento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(CadastrarClienteActivity.this, s, Toast.LENGTH_SHORT);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Spinner spinnerEstado;
        spinnerEstado = findViewById(R.id.spinnerEstado);
        ArrayAdapter<EstadosEnum> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, EstadosEnum.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResourceId() {
        // Return the layout for this specific activity
        return R.layout.activity_cadastrar_clientes; // Replace with your actual layout
    }
}
