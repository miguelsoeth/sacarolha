package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.VendaItem;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.model.ProdutoTotal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends AbstrataDAO {
    // Add user_id to the list of columns
    private final String[] colunas = {
            Cliente.COLUNA_ID,
            Cliente.COLUNA_NOME,
            Cliente.COLUNA_DOCUMENTO,
            Cliente.COLUNA_TELEFONE,
            Cliente.COLUNA_EMAIL,
            Cliente.COLUNA_RUA,
            Cliente.COLUNA_BAIRRO,
            Cliente.COLUNA_COMPLEMENTO,
            Cliente.COLUNA_NUMERO,
            Cliente.COLUNA_CIDADE,
            Cliente.COLUNA_ESTADO,
            Cliente.COLUNA_USER_ID // Foreign key to User table
    };

    String userId;

    public ClienteDAO(Context context) {
        db_helper = new DBOpenHelper(context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = preferences.getString(Shared.KEY_USER_ID, "");
    }

    public Cliente getClienteForVenda(Venda venda) {
        Cliente cliente = null;
        try {
            Open();

            String clienteId = venda.getClienteId();
            if (clienteId != null && !clienteId.isEmpty()) {
                cliente = selectById(clienteId);
            }
        } finally {
            Close(); // Close the database connection
        }

        return cliente;
    }

    public List<ProdutoTotal> getProdutosTotalGastoByCliente(String clienteId) {
        List<ProdutoTotal> produtosTotais = new ArrayList<>();

        String query = "SELECT tvinho.nome AS produto_nome, " +
                "SUM(titemv.quantidade) AS total_quantidade, " +
                "SUM(titemv.preco_total) AS total_gasto " +
                "FROM " + VendaItem.TABLE_NAME + " titemv " +
                "JOIN " + Venda.TABLE_NAME + " tvenda ON tvenda." + Venda.COLUNA_ID + " = titemv." + VendaItem.COLUNA_VENDA_ID + " " +
                "JOIN " + Vinho.TABLE_NAME + " tvinho ON tvinho." + Vinho.COLUNA_ID + " = titemv." + VendaItem.COLUNA_PRODUTO_ID + " " +
                "WHERE tvenda." + Venda.COLUNA_CLIENTE_ID + " = ? " +
                "GROUP BY " + VendaItem.COLUNA_PRODUTO_ID;

        Cursor cursor = null;

        try {
            Open();
            cursor = db.rawQuery(query, new String[]{clienteId});

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    ProdutoTotal produtoTotal = new ProdutoTotal();
                    produtoTotal.setProdutoNome(cursor.getString(cursor.getColumnIndexOrThrow("produto_nome")));
                    produtoTotal.setTotalQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow("total_quantidade")));

                    double totalGastoRaw = cursor.getDouble(cursor.getColumnIndexOrThrow("total_gasto"));
                    BigDecimal totalGastoRounded = new BigDecimal(totalGastoRaw).setScale(2, RoundingMode.HALF_UP);

                    produtoTotal.setTotalGasto(totalGastoRounded.doubleValue());
                    produtosTotais.add(produtoTotal);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return produtosTotais;
    }

    // Insert method, including foreign key reference for user_id
    public long insert(Cliente cliente) {
        long insertRows = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Cliente.COLUNA_ID, cliente.getId());
            contentValues.put(Cliente.COLUNA_NOME, cliente.getNome());
            contentValues.put(Cliente.COLUNA_DOCUMENTO, cliente.getDocumento());
            contentValues.put(Cliente.COLUNA_TELEFONE, cliente.getTelefone());
            contentValues.put(Cliente.COLUNA_EMAIL, cliente.getEmail());
            contentValues.put(Cliente.COLUNA_RUA, cliente.getRua());
            contentValues.put(Cliente.COLUNA_BAIRRO, cliente.getBairro());
            contentValues.put(Cliente.COLUNA_COMPLEMENTO, cliente.getComplemento());
            contentValues.put(Cliente.COLUNA_NUMERO, cliente.getNumero());
            contentValues.put(Cliente.COLUNA_CIDADE, cliente.getCidade());
            contentValues.put(Cliente.COLUNA_ESTADO, cliente.getEstado());
            contentValues.put(Cliente.COLUNA_USER_ID, cliente.getUserId()); // Foreign key reference

            insertRows = db.insert(Cliente.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    // Get Cliente by ID, including the user_id foreign key
    public Cliente selectById(String id) {
        Cliente cliente = null;
        try {
            Open();

            String selection = Cliente.COLUNA_ID + " = ? AND " + Cliente.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {id, userId};

            Cursor cursor = db.query(Cliente.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);


            if (cursor != null && cursor.moveToFirst()) {
                cliente = new Cliente();
                cliente.setId(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ID)));
                cliente.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NOME)));
                cliente.setDocumento(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_DOCUMENTO)));
                cliente.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_TELEFONE)));
                cliente.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_EMAIL)));
                cliente.setRua(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_RUA)));
                cliente.setBairro(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_BAIRRO)));
                cliente.setComplemento(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_COMPLEMENTO)));
                cliente.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NUMERO)));
                cliente.setCidade(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_CIDADE)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ESTADO)));
                cliente.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_USER_ID))); // Retrieve user_id
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return cliente;
    }

    // Update method including foreign key reference for user_id
    public int update(Cliente cliente) {
        int rowsAffected = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Cliente.COLUNA_NOME, cliente.getNome());
            contentValues.put(Cliente.COLUNA_DOCUMENTO, cliente.getDocumento());
            contentValues.put(Cliente.COLUNA_TELEFONE, cliente.getTelefone());
            contentValues.put(Cliente.COLUNA_EMAIL, cliente.getEmail());
            contentValues.put(Cliente.COLUNA_RUA, cliente.getRua());
            contentValues.put(Cliente.COLUNA_BAIRRO, cliente.getBairro());
            contentValues.put(Cliente.COLUNA_COMPLEMENTO, cliente.getComplemento());
            contentValues.put(Cliente.COLUNA_NUMERO, cliente.getNumero());
            contentValues.put(Cliente.COLUNA_CIDADE, cliente.getCidade());
            contentValues.put(Cliente.COLUNA_ESTADO, cliente.getEstado());
            contentValues.put(Cliente.COLUNA_USER_ID, cliente.getUserId()); // Update foreign key reference

            String whereClause = Cliente.COLUNA_ID + " = ? AND " + Cliente.COLUNA_USER_ID + " = ?";
            String[] whereArgs = {cliente.getId(), userId};

            rowsAffected = db.update(Cliente.TABLE_NAME, contentValues, whereClause, whereArgs);
        } finally {
            Close();
        }

        return rowsAffected;
    }

    // Delete method
    public int delete(String id) {
        int rowsDeleted = 0;
        try {
            Open();
            String whereClause = Cliente.COLUNA_ID + " = ? AND " + Cliente.COLUNA_USER_ID + " = ?";
            String[] whereArgs = {id, userId};

            rowsDeleted = db.delete(Cliente.TABLE_NAME, whereClause, whereArgs);
        } finally {
            Close();
        }

        return rowsDeleted;
    }

    // Select all Clientes including the foreign key user_id
    public List<Cliente> selectAll() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            Open();
            String selection = Cliente.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {userId};

            Cursor cursor = db.query(Cliente.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ID)));
                    cliente.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NOME)));
                    cliente.setDocumento(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_DOCUMENTO)));
                    cliente.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_TELEFONE)));
                    cliente.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_EMAIL)));
                    cliente.setRua(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_RUA)));
                    cliente.setBairro(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_BAIRRO)));
                    cliente.setComplemento(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_COMPLEMENTO)));
                    cliente.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NUMERO)));
                    cliente.setCidade(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_CIDADE)));
                    cliente.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ESTADO)));
                    cliente.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_USER_ID))); // Retrieve user_id
                    clientes.add(cliente);
                }
                cursor.close();
            }
        } finally {
            Close();
        }

        return clientes;
    }
}
