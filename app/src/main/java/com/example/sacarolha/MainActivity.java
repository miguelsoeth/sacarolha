package com.example.sacarolha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.enums.EstadosEnum;
import com.example.sacarolha.util.enums.TiposVinhoEnum;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageView btnHome, btnClientes, btnVinhos, btnVender;
    TextView topbarUsername;
    LinearLayout perfilGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new HomeFragment());

        btnHome = findViewById(R.id.btnHome);
        btnHome.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.light_gray));
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnHome);
                replaceFragment(new HomeFragment());
            }
        });

        btnClientes = findViewById(R.id.btnClientes);
        btnClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnClientes);
                replaceFragment(new ClientesFragment());
            }
        });

        btnVinhos = findViewById(R.id.btnVinhos);
        btnVinhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnVinhos);
                replaceFragment(new VinhosFragment());
            }
        });

        btnVender = findViewById(R.id.btnVender);
        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnVender);
                replaceFragment(new VenderFragment());
            }
        });

        topbarUsername = findViewById(R.id.topbarUsername);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = preferences.getString(Shared.KEY_USERNAME, "");
        topbarUsername.setText(username);

        perfilGroup = findViewById(R.id.perfilGroup);
        perfilGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_profile, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_logout) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.remove(Shared.KEY_USUARIO);
                    edit.remove(Shared.KEY_SENHA);
                    edit.apply();

                    Intent it = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void setActiveButton(ImageView activeButton) {
        resetButtonColorState();
        activeButton.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.light_gray));
    }
    private void resetButtonColorState() {
        btnHome.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
        btnClientes.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
        btnVinhos.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
        btnVender.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
    }
}