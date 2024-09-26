package com.example.sacarolha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sacarolha.database.dao.UserDAO;
import com.example.sacarolha.database.model.User;

public class RegisterActivity  extends AppCompatActivity {
    Button btnRegistrar;

    EditText editUser, editPassword, editNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        editNome = findViewById(R.id.editNome);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = editUser.getText().toString();
                String senha = editPassword.getText().toString();
                String nome = editNome.getText().toString();

                boolean error = false;

                if (usuario.isEmpty()) {
                    editUser.setError("Campo obrigatório");
                    error = true;
                }

                if (senha.isEmpty()) {
                    editPassword.setError("Campo obrigatório");
                    error = true;
                }

                if (nome.isEmpty()) {
                    editNome.setError("Campo obrigatório");
                    error = true;
                }

                if (error) {
                    return;
                }

                UserDAO userDAO = new UserDAO(RegisterActivity.this);
                User u = new User(nome, usuario, senha);
                userDAO.insert(u);

                Toast.makeText(RegisterActivity.this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Shared.KEY_FIRST_RUN, false);
                editor.apply();

                sendBackToLogin();
            }
        });

    }

    private void sendBackToLogin() {
        Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(it);
        finish();
    }
}
