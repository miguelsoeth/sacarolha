package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
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
            Vinho.COLUNA_CODIGO,
            Vinho.COLUNA_USER_ID  // New column for user ID (foreign key)
    };

    String userId;

    public VinhoDAO(Context context) {
        db_helper = new DBOpenHelper(context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = preferences.getString(Shared.KEY_USER_ID, "");
    }

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
            contentValues.put(Vinho.COLUNA_CODIGO, vinho.getCodigo());
            contentValues.put(Vinho.COLUNA_USER_ID, vinho.getUserId());  // Set user ID

            insertRows = db.insert(Vinho.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    public int diminuirEstoque(String id, int quantidade) {
        int rowsAffected = 0;
        try {
            Open();

            Vinho vinho = selectById(id);
            if (vinho != null) {
                int estoqueAtual = vinho.getEstoque();
                int novoEstoque = estoqueAtual - quantidade;

                if (novoEstoque < 0) {
                    novoEstoque = 0;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(Vinho.COLUNA_ESTOQUE, novoEstoque);

                rowsAffected = db.update(Vinho.TABLE_NAME, contentValues, Vinho.COLUNA_ID + " = ?", new String[]{id});
            }
        } finally {
            Close();
        }

        return rowsAffected;
    }

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
            contentValues.put(Vinho.COLUNA_CODIGO, vinho.getCodigo());
            contentValues.put(Vinho.COLUNA_USER_ID, vinho.getUserId());  // Update user ID

            rowsAffected = db.update(Vinho.TABLE_NAME, contentValues, Vinho.COLUNA_ID + " = ?",
                    new String[]{vinho.getId()});
        } finally {
            Close();
        }

        return rowsAffected;
    }

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

    public List<Vinho> selectAll() {
        List<Vinho> vinhos = new ArrayList<>();
        try {
            Open();
            String selection = Vinho.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {userId};

            Cursor cursor = db.query(Vinho.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Vinho vinho = new Vinho();
                    vinho.setId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ID)));
                    vinho.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_NOME)));
                    vinho.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_TIPO)));
                    vinho.setSafra(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)) ? null : cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)));
                    vinho.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(Vinho.COLUNA_PRECO)));
                    vinho.setEstoque(cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ESTOQUE)));
                    vinho.setCodigo(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)) ? null : cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)));
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

    public Vinho selectById(String id) {
        Vinho vinho = null;
        try {
            Open();

            String selection = Vinho.COLUNA_ID + " = ? AND " + Vinho.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {id, userId};

            Cursor cursor = db.query(Vinho.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vinho = new Vinho();
                vinho.setId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ID)));
                vinho.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_NOME)));
                vinho.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_TIPO)));
                vinho.setSafra(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)) ? null : cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)));
                vinho.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(Vinho.COLUNA_PRECO)));
                vinho.setEstoque(cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ESTOQUE)));
                vinho.setCodigo(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)) ? null : cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)));
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

    public Vinho selectByCodigo(String codigo) {
        Vinho vinho = null;
        try {
            Open();

            String selection = Vinho.COLUNA_CODIGO + " = ? AND " + Vinho.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {codigo, userId};

            Cursor cursor = db.query(Vinho.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vinho = new Vinho();
                vinho.setId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ID)));
                vinho.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_NOME)));
                vinho.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_TIPO)));
                vinho.setSafra(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)) ? null : cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)));
                vinho.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(Vinho.COLUNA_PRECO)));
                vinho.setEstoque(cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ESTOQUE)));
                vinho.setCodigo(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)) ? null : cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)));
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

    public Vinho selectByCodigoWithId(String codigo, String id) {
        Vinho vinho = null;
        try {
            Open();

            String selection = Vinho.COLUNA_CODIGO + " = ? AND " + Vinho.COLUNA_ID + " != ? AND "+ Vinho.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {codigo, id, userId};

            Cursor cursor = db.query(Vinho.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vinho = new Vinho();
                vinho.setId(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ID)));
                vinho.setNome(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_NOME)));
                vinho.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_TIPO)));
                vinho.setSafra(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)) ? null : cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_SAFRA)));
                vinho.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(Vinho.COLUNA_PRECO)));
                vinho.setEstoque(cursor.getInt(cursor.getColumnIndexOrThrow(Vinho.COLUNA_ESTOQUE)));
                vinho.setCodigo(cursor.isNull(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)) ? null : cursor.getString(cursor.getColumnIndexOrThrow(Vinho.COLUNA_CODIGO)));
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
}
