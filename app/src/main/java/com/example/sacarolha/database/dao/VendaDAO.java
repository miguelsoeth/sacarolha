package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.util.Shared;

import java.util.ArrayList;
import java.util.List;

public class VendaDAO extends AbstrataDAO {
    // Columns in the Venda table
    private final String[] colunas = {
            Venda.COLUNA_ID,
            Venda.COLUNA_DATA,
            Venda.COLUNA_TOTAL,
            Venda.COLUNA_CLIENTE_ID,
            Venda.COLUNA_USER_ID
    };

    String userId;

    public VendaDAO(Context context) {
        db_helper = new DBOpenHelper(context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = preferences.getString(Shared.KEY_USER_ID, "");
    }

    // Insert method for Venda
    public long insert(Venda venda) {
        long insertRows = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Venda.COLUNA_ID, venda.getId());
            contentValues.put(Venda.COLUNA_DATA, venda.getData());
            contentValues.put(Venda.COLUNA_TOTAL, venda.getTotal());
            contentValues.put(Venda.COLUNA_CLIENTE_ID, venda.getClienteId());
            contentValues.put(Venda.COLUNA_USER_ID, venda.getUserId());

            insertRows = db.insert(Venda.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    // Get all Vendas
    public List<Venda> selectAll() {
        List<Venda> vendas = new ArrayList<>();
        try {
            Open();

            // Define the selection criteria (filter by userId)
            String selection = Venda.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {userId};

            // Query the database
            Cursor cursor = db.query(Venda.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            // Process the results
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Venda venda = new Venda();
                    venda.setId(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_ID)));
                    venda.setData(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_DATA)));
                    venda.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(Venda.COLUNA_TOTAL)));
                    venda.setClienteId(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_CLIENTE_ID)));
                    venda.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_USER_ID)));
                    vendas.add(venda);
                }
                cursor.close();
            }
        } finally {
            Close();
        }

        return vendas;
    }

    // Update Venda
    public int update(Venda venda) {
        int rowsAffected = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Venda.COLUNA_DATA, venda.getData());
            contentValues.put(Venda.COLUNA_TOTAL, venda.getTotal());
            contentValues.put(Venda.COLUNA_CLIENTE_ID, venda.getClienteId());
            contentValues.put(Venda.COLUNA_USER_ID, venda.getUserId());

            rowsAffected = db.update(Venda.TABLE_NAME, contentValues, Venda.COLUNA_ID + " = ?",
                    new String[]{venda.getId()});
        } finally {
            Close();
        }

        return rowsAffected;
    }

    // Delete Venda by ID
    public int delete(String id) {
        int rowsDeleted = 0;
        try {
            Open();

            // Define the selection criteria (filter by id and userId)
            String whereClause = Venda.COLUNA_ID + " = ? AND " + Venda.COLUNA_USER_ID + " = ?";
            String[] whereArgs = {id, userId};

            // Perform the delete operation
            rowsDeleted = db.delete(Venda.TABLE_NAME, whereClause, whereArgs);
        } finally {
            Close();
        }

        return rowsDeleted;
    }

}
