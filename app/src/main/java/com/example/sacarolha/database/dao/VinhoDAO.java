package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.enums.TiposVinhoEnum;

import java.util.ArrayList;
import java.util.List;

public class VinhoDAO extends AbstrataDAO {
    private final String[] colunas = {
            Vinho.COLUNA_ID,
            Vinho.COLUNA_NOME,
            Vinho.COLUNA_TIPO,
            Vinho.COLUNA_SAFRA,
            Vinho.COLUNA_PRECO,
            Vinho.COLUNA_ESTOQUE,
            Vinho.COLUNA_USER_ID  // New column for user ID (foreign key)
    };

    public VinhoDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    // Insert method
    public long insert(Vinho vinho) {
        long insertRows = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Vinho.COLUNA_ID, vinho.getId());
            contentValues.put(Vinho.COLUNA_NOME, vinho.getNome());
            contentValues.put(Vinho.COLUNA_TIPO, vinho.getTipo());
            contentValues.put(Vinho.COLUNA_SAFRA, vinho.getSafra());
            contentValues.put(Vinho.COLUNA_PRECO, vinho.getPreco());
            contentValues.put(Vinho.COLUNA_ESTOQUE, vinho.getEstoque());
            contentValues.put(Vinho.COLUNA_USER_ID, vinho.getUserId());  // Set user ID

            insertRows = db.insert(Vinho.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    // Get Vinho by ID
    public Vinho selectById(String id) {
        Vinho vinho = null;
        try {
            Open();

            String selection = Vinho.COLUNA_ID + " = ?";
            String[] selectionArgs = {id};

            Cursor cursor = db.query(Vinho.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vinho = new Vinho();
                vinho.setId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ID)));
                vinho.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_NOME)));
                vinho.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_TIPO)));
                vinho.setSafra(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)) ? null : cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)));
                vinho.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(Vinho.COLUNA_PRECO)));
                vinho.setEstoque(cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ESTOQUE)));
                vinho.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_USER_ID)));  // Retrieve user ID
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return vinho;
    }

    // Update method
    public int update(Vinho vinho) {
        int rowsAffected = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Vinho.COLUNA_NOME, vinho.getNome());
            contentValues.put(Vinho.COLUNA_TIPO, vinho.getTipo());
            contentValues.put(Vinho.COLUNA_SAFRA, vinho.getSafra());
            contentValues.put(Vinho.COLUNA_PRECO, vinho.getPreco());
            contentValues.put(Vinho.COLUNA_ESTOQUE, vinho.getEstoque());
            contentValues.put(Vinho.COLUNA_USER_ID, vinho.getUserId());  // Update user ID

            rowsAffected = db.update(Vinho.TABLE_NAME, contentValues, Vinho.COLUNA_ID + " = ?",
                    new String[]{vinho.getId()});
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
            rowsDeleted = db.delete(Vinho.TABLE_NAME, Vinho.COLUNA_ID + " = ?",
                    new String[]{id});
        } finally {
            Close();
        }

        return rowsDeleted;
    }

    // Select all Vinho
    public List<Vinho> selectAll() {
        List<Vinho> vinhos = new ArrayList<>();
        try {
            Open();
            Cursor cursor = db.query(Vinho.TABLE_NAME, colunas, null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Vinho vinho = new Vinho();
                    vinho.setId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ID)));
                    vinho.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_NOME)));
                    vinho.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_TIPO)));
                    vinho.setSafra(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)) ? null : cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)));
                    vinho.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(Vinho.COLUNA_PRECO)));
                    vinho.setEstoque(cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ESTOQUE)));
                    vinho.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_USER_ID)));  // Set user ID
                    vinhos.add(vinho);
                }
                cursor.close();
            }
        } finally {
            Close();
        }

        return vinhos;
    }
}
