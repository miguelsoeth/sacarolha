package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Cidade;
import com.example.sacarolha.database.model.Estado;

import java.util.ArrayList;
import java.util.List;

public class CidadeDAO extends AbstrataDAO {
    private final String[] colunas = {
            Cidade.COLUNA_ID,
            Cidade.COLUNA_ESTADO,
            Cidade.COLUNA_UF,
            Cidade.COLUNA_NOME
    };

    public CidadeDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    // Insert method
    public long insert(Cidade cidade) {
        long insertRows = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Cidade.COLUNA_ESTADO, cidade.getEstado());
            contentValues.put(Cidade.COLUNA_UF, cidade.getUf());
            contentValues.put(Cidade.COLUNA_NOME, cidade.getNome());

            insertRows = db.insert(Cidade.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    // Get Cidade by ID
    public Cidade selectById(int id) {
        Cidade cidade = null;
        try {
            Open();

            String selection = Cidade.COLUNA_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            Cursor cursor = db.query(Cidade.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                cidade = new Cidade();
                cidade.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Cidade.COLUNA_ID)));
                cidade.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(Cidade.COLUNA_ESTADO)));
                cidade.setUf(cursor.getString(cursor.getColumnIndexOrThrow(Cidade.COLUNA_UF)));
                cidade.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Cidade.COLUNA_NOME)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return cidade;
    }

    // Select all Cidades
    public List<Cidade> selectAll() {
        List<Cidade> cidades = new ArrayList<>();
        try {
            Open();
            Cursor cursor = db.query(Cidade.TABLE_NAME, colunas, null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Cidade cidade = new Cidade();
                    cidade.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Cidade.COLUNA_ID)));
                    cidade.setEstado(cursor.getInt(cursor.getColumnIndexOrThrow(Cidade.COLUNA_ESTADO)));
                    cidade.setUf(cursor.getString(cursor.getColumnIndexOrThrow(Cidade.COLUNA_UF)));
                    cidade.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Cidade.COLUNA_NOME)));
                    cidades.add(cidade);
                }
                cursor.close();
            }
        } finally {
            Close();
        }

        return cidades;
    }
}