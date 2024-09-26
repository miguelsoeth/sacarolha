package com.example.sacarolha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sacarolha.database.dao.UserDAO;
import com.example.sacarolha.database.model.User;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    Button btnEntrar;

    EditText editUser, editPassword;
    CheckBox checkLembrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        boolean isFirstRun = preferences.getBoolean(Shared.KEY_FIRST_RUN, true);

        if (isFirstRun) {
            registerOnFirstRun();
        }

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);

        checkLembrar = findViewById(R.id.checkLembrar);

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = editUser.getText().toString();
                String senha = editPassword.getText().toString();
                boolean lembrarUser = checkLembrar.isChecked();

                if (verificarCredenciais(usuario, senha)) {
                    if (lembrarUser) {
                        setAutoLogin(usuario, senha);
                    }
                    acceptLogin();
                }
            }
        });

        autoLogin();
    }

    private boolean verificarCredenciais(String usuario, String senha) {
        if ( usuario.isEmpty() && senha.isEmpty() ) {
            editUser.setError("Campo obrigatório");
            editPassword.setError("Campo obrigatório");
            return false;
        }

        if ( usuario.isEmpty() ) {
            editUser.setError("Campo obrigatório");
            return false;
        }

        if ( senha.isEmpty() ) {
            editPassword.setError("Campo obrigatório");
            return false;
        }

        UserDAO userDAO = new UserDAO(LoginActivity.this);
        User u = userDAO.getUserByUsername(usuario);

        if(u == null) {
            Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            String hashSenha = PasswordHandler.hashPassword(senha);
            if (!hashSenha.equals(u.getSenha())) {
                Toast.makeText(this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(Shared.KEY_USERNAME, u.getNome().split(" ")[0]);
        edit.apply();
        return true;
    }

    private void setAutoLogin(String usuario, String senha) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(Shared.KEY_USUARIO, usuario);
        edit.putString(Shared.KEY_SENHA, senha);
        edit.apply();
    }

    private void autoLogin() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String usuario = preferences.getString(Shared.KEY_USUARIO, "");
        String senha = preferences.getString(Shared.KEY_SENHA, "");
        editUser.setText(usuario);
        editPassword.setText(senha);
        if (!usuario.isEmpty() || !senha.isEmpty()) {
            btnEntrar.performClick();
        }
    }

    private void acceptLogin() {
        Intent it = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(it);
        finish();
    }

    private void registerOnFirstRun() {
        Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(it);
        finish();
    }

}
