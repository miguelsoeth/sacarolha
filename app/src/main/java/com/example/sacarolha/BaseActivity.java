package com.example.sacarolha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sacarolha.util.Shared;

public abstract class BaseActivity extends AppCompatActivity {

    private ImageView btnHome, btnClientes, btnVinhos, btnVender;
    TextView topbarUsername;
    LinearLayout perfilGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // Set the base layout
        setBaseContent(getLayoutResourceId());

        btnHome = findViewById(R.id.btnHome);
        btnClientes = findViewById(R.id.btnClientes);
        btnVinhos = findViewById(R.id.btnVinhos);
        btnVender = findViewById(R.id.btnVender);
        perfilGroup = findViewById(R.id.perfilGroup);
        topbarUsername = findViewById(R.id.topbarUsername);

        perfilGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        btnHome.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.light_gray));
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnHome);
                setBaseContent(R.layout.activity_main);
            }
        });

        btnClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnClientes);
                setBaseContent(R.layout.activity_clientes);
            }
        });

        btnVinhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnVinhos);
                setBaseContent(R.layout.activity_vinhos);
            }
        });

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton(btnVender);
                setBaseContent(R.layout.activity_vender);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        String username = preferences.getString(Shared.KEY_USERNAME, "");
        topbarUsername.setText(username);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_profile, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_logout) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.remove(Shared.KEY_USUARIO);
                    edit.remove(Shared.KEY_SENHA);
                    edit.apply();

                    Intent it = new Intent(BaseActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    // Abstract method to be overridden by child activities
    protected abstract int getLayoutResourceId();

    private void setBaseContent(int contentId) {
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        LayoutInflater inflater = LayoutInflater.from(this);
        View childLayout = inflater.inflate(contentId, contentFrame, false);
        contentFrame.addView(childLayout);
    }

    private void setActiveButton(ImageView activeButton) {
        resetButtonColorState();
        activeButton.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.light_gray));
    }
    private void resetButtonColorState() {
        btnHome.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.black));
        btnClientes.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.black));
        btnVinhos.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.black));
        btnVender.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.black));
    }
}
