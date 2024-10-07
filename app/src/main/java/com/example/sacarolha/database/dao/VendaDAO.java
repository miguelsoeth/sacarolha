package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Venda;

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

    public VendaDAO(Context context) {
        db_helper = new DBOpenHelper(context);
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

    // Get Venda by ID
    public Venda selectById(String id) {
        Venda venda = null;
        try {
            Open();

            String selection = Venda.COLUNA_ID + " = ?";
            String[] selectionArgs = {id};

            Cursor cursor = db.query(Venda.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                venda = new Venda();
                venda.setId(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_ID)));
                venda.setData(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_DATA)));
                venda.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(Venda.COLUNA_TOTAL)));
                venda.setClienteId(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_CLIENTE_ID)));
                venda.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(Venda.COLUNA_USER_ID)));
            }

            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return venda;
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
            rowsDeleted = db.delete(Venda.TABLE_NAME, Venda.COLUNA_ID + " = ?",
                    new String[]{id});
        } finally {
            Close();
        }

        return rowsDeleted;
    }

    // Get all Vendas
    public List<Venda> selectAll() {
        List<Venda> vendas = new ArrayList<>();
        try {
            Open();
            Cursor cursor = db.query(Venda.TABLE_NAME, colunas, null, null, null, null, null);

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
}
