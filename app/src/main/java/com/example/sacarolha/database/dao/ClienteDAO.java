package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends AbstrataDAO {
    private final String[] colunas = {
            Cliente.COLUNA_ID,
            Cliente.COLUNA_NOME,
            Cliente.COLUNA_DOCUMENTO,
            Cliente.COLUNA_TELEFONE,
            Cliente.COLUNA_EMAIL,
            Cliente.COLUNA_LOGRADOURO,
            Cliente.COLUNA_NUMERO,
            Cliente.COLUNA_CIDADE,
            Cliente.COLUNA_ESTADO
    };

    public ClienteDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    // Insert method
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
            contentValues.put(Cliente.COLUNA_LOGRADOURO, cliente.getLogradouro());
            contentValues.put(Cliente.COLUNA_NUMERO, cliente.getNumero());
            contentValues.put(Cliente.COLUNA_CIDADE, cliente.getCidade());
            contentValues.put(Cliente.COLUNA_ESTADO, cliente.getEstado());

            insertRows = db.insert(Cliente.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    // Get Cliente by ID
    public Cliente selectById(String id) {
        Cliente cliente = null;
        try {
            Open();

            String selection = Cliente.COLUNA_ID + " = ?";
            String[] selectionArgs = {id};

            Cursor cursor = db.query(Cliente.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                cliente = new Cliente();
                cliente.setId(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ID)));
                cliente.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NOME)));
                cliente.setDocumento(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_DOCUMENTO)));
                cliente.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_TELEFONE)));
                cliente.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_EMAIL)));
                cliente.setLogradouro(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_LOGRADOURO)));
                cliente.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NUMERO)));
                cliente.setCidade(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_CIDADE)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ESTADO)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return cliente;
    }

    // Update method
    public int update(Cliente cliente) {
        int rowsAffected = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Cliente.COLUNA_NOME, cliente.getNome());
            contentValues.put(Cliente.COLUNA_DOCUMENTO, cliente.getDocumento());
            contentValues.put(Cliente.COLUNA_TELEFONE, cliente.getTelefone());
            contentValues.put(Cliente.COLUNA_EMAIL, cliente.getEmail());
            contentValues.put(Cliente.COLUNA_LOGRADOURO, cliente.getLogradouro());
            contentValues.put(Cliente.COLUNA_NUMERO, cliente.getNumero());
            contentValues.put(Cliente.COLUNA_CIDADE, cliente.getCidade());
            contentValues.put(Cliente.COLUNA_ESTADO, cliente.getEstado());

            rowsAffected = db.update(Cliente.TABLE_NAME, contentValues, Cliente.COLUNA_ID + " = ?",
                    new String[]{cliente.getId()});
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
            rowsDeleted = db.delete(Cliente.TABLE_NAME, Cliente.COLUNA_ID + " = ?",
                    new String[]{id});
        } finally {
            Close();
        }

        return rowsDeleted;
    }

    // Select all Clientes
    public List<Cliente> selectAll() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            Open();
            Cursor cursor = db.query(Cliente.TABLE_NAME, colunas, null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ID)));
                    cliente.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NOME)));
                    cliente.setDocumento(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_DOCUMENTO)));
                    cliente.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_TELEFONE)));
                    cliente.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_EMAIL)));
                    cliente.setLogradouro(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_LOGRADOURO)));
                    cliente.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_NUMERO)));
                    cliente.setCidade(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_CIDADE)));
                    cliente.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(Cliente.COLUNA_ESTADO)));
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
