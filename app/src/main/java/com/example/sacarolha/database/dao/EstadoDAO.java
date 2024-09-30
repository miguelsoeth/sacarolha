package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Estado;

import java.util.ArrayList;
import java.util.List;

public class EstadoDAO extends AbstrataDAO {
    private final String[] colunas = {
            Estado.COLUNA_ID,
            Estado.COLUNA_UF,
            Estado.COLUNA_NOME
    };

    public EstadoDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    // Insert method
    public long insert(Estado estado) {
        long insertRows = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Estado.COLUNA_UF, estado.getUf());
            contentValues.put(Estado.COLUNA_NOME, estado.getNome());

            insertRows = db.insert(Estado.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    // Get Estado by ID
    public Estado selectById(int id) {
        Estado estado = null;
        try {
            Open();

            String selection = Estado.COLUNA_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            Cursor cursor = db.query(Estado.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                estado = new Estado();
                estado.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Estado.COLUNA_ID)));
                estado.setUf(cursor.getString(cursor.getColumnIndexOrThrow(Estado.COLUNA_UF)));
                estado.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Estado.COLUNA_NOME)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return estado;
    }

    // Select all Estados
    public List<Estado> selectAll() {
        List<Estado> estados = new ArrayList<>();
        try {
            Open();
            Cursor cursor = db.query(Estado.TABLE_NAME, colunas, null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Estado estado = new Estado();
                    estado.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Estado.COLUNA_ID)));
                    estado.setUf(cursor.getString(cursor.getColumnIndexOrThrow(Estado.COLUNA_UF)));
                    estado.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Estado.COLUNA_NOME)));
                    estados.add(estado);
                }
                cursor.close();
            }
        } finally {
            Close();
        }

        return estados;
    }
}
