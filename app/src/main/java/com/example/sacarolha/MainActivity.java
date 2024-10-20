package com.example.sacarolha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sacarolha.fragment.ClientesFragment;
import com.example.sacarolha.fragment.HomeFragment;
import com.example.sacarolha.fragment.VendasFragment;
import com.example.sacarolha.fragment.VinhosFragment;
import com.example.sacarolha.util.Shared;

public class MainActivity extends AppCompatActivity {

    private ImageView iconRelatorios, iconClientes, iconVinhos, iconVender;
    private LinearLayout btnRelatorios, btnClientes, btnVinhos, btnVender;
    TextView topbarUsername;
    LinearLayout perfilGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new HomeFragment());

        btnRelatorios = findViewById(R.id.btnRelatorios);
        btnClientes = findViewById(R.id.btnClientes);
        btnVinhos = findViewById(R.id.btnVinhos);
        btnVender = findViewById(R.id.btnVender);

        iconRelatorios = findViewById(R.id.iconRelatorios);
        iconRelatorios.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.light_gray));
        btnRelatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(iconRelatorios);
                replaceFragment(new HomeFragment());
            }
        });

        iconClientes = findViewById(R.id.iconClientes);
        btnClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(iconClientes);
                replaceFragment(new ClientesFragment());
            }
        });

        iconVinhos = findViewById(R.id.iconVinhos);
        btnVinhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(iconVinhos);
                replaceFragment(new VinhosFragment());
            }
        });

        iconVender = findViewById(R.id.iconVender);
        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(iconVender);
                replaceFragment(new VendasFragment());
            }
        });

        topbarUsername = findViewById(R.id.topbarUsername);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = preferences.getString(Shared.KEY_USERNAME, "");
        topbarUsername.setText(Html.fromHtml(String.format(getString(R.string.ola), username), Html.FROM_HTML_MODE_LEGACY));

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
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void setActiveButton(ImageView activeButton) {
        resetButtonColorState();
        activeButton.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.light_gray));
    }
    private void resetButtonColorState() {
        iconRelatorios.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
        iconClientes.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
        iconVinhos.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
        iconVender.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black));
    }
}