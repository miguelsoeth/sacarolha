package com.example.sacarolha;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sacarolha.database.dao.ClienteDAO;
import com.example.sacarolha.database.dao.VinhoDAO;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.enums.EstadosEnum;
import com.example.sacarolha.util.enums.TiposVinhoEnum;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ClienteDAO clienteDAO = new ClienteDAO(MainActivity.this);
//        VinhoDAO vinhoDAO = new VinhoDAO(MainActivity.this);
//
//        // Create 3 Cliente instances
//        Cliente cliente1 = new Cliente("João Silva", "12345678901", "11987654321", "Rua A", "123", "São Paulo", EstadosEnum.SP.name(), "joao@email.com");
//        Cliente cliente2 = new Cliente("Maria Oliveira", "98765432100", "11976543210", "Rua B", "456", "Rio de Janeiro", EstadosEnum.RJ.name(), "maria@email.com");
//        Cliente cliente3 = new Cliente("Carlos Pereira", "45678912300", "11965432109", "Rua C", "789", "Belo Horizonte", EstadosEnum.MG.name(), null);
//
//        // Insert Cliente instances into the database
//        clienteDAO.insert(cliente1);
//        clienteDAO.insert(cliente2);
//        clienteDAO.insert(cliente3);
//
//        // Create 3 Vinho instances
//        Vinho vinho1 = new Vinho("Vinho Tinto", TiposVinhoEnum.TINTO.name(), 2020, 49.99, 100);
//        Vinho vinho2 = new Vinho("Vinho Branco", TiposVinhoEnum.TINTO.name(), null, 39.99, 50);
//        Vinho vinho3 = new Vinho("Vinho Rosé", TiposVinhoEnum.TINTO.name(), 2021, 45.00, 75);
//
//        // Insert Vinho instances into the database
//        vinhoDAO.insert(vinho1);
//        vinhoDAO.insert(vinho2);
//        vinhoDAO.insert(vinho3);

    }

    @Override
    protected int getLayoutResourceId() {
        // Return the layout for this specific activity
        return R.layout.activity_main; // Replace with your actual layout
    }
}