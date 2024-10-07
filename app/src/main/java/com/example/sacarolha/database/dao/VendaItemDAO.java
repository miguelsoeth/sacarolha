package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.VendaItem;

import java.util.ArrayList;
import java.util.List;

public class VendaItemDAO extends AbstrataDAO {
    private final String[] colunas = {
            VendaItem.COLUNA_ID,
            VendaItem.COLUNA_VENDA_ID,
            VendaItem.COLUNA_PRODUTO_ID,
            VendaItem.COLUNA_QUANTIDADE,
            VendaItem.COLUNA_PRECO
    };

    public VendaItemDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    // Insert method, including foreign keys venda_id and produto_id
    public long insert(VendaItem vendaItem) {
        long insertRows = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(VendaItem.COLUNA_ID, vendaItem.getId());
            contentValues.put(VendaItem.COLUNA_VENDA_ID, vendaItem.getVendaId());
            contentValues.put(VendaItem.COLUNA_PRODUTO_ID, vendaItem.getProdutoId());
            contentValues.put(VendaItem.COLUNA_QUANTIDADE, vendaItem.getQuantidade());
            contentValues.put(VendaItem.COLUNA_PRECO, vendaItem.getPreco());

            insertRows = db.insert(VendaItem.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    // Get VendaItem by ID
    public VendaItem selectById(String id) {
        VendaItem vendaItem = null;
        try {
            Open();

            String selection = VendaItem.COLUNA_ID + " = ?";
            String[] selectionArgs = {id};

            Cursor cursor = db.query(VendaItem.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vendaItem = new VendaItem();
                vendaItem.setId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_ID)));
                vendaItem.setVendaId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_VENDA_ID)));
                vendaItem.setProdutoId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRODUTO_ID)));
                vendaItem.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_QUANTIDADE)));
                vendaItem.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return vendaItem;
    }

    // Update method for VendaItem
    public int update(VendaItem vendaItem) {
        int rowsAffected = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(VendaItem.COLUNA_VENDA_ID, vendaItem.getVendaId());
            contentValues.put(VendaItem.COLUNA_PRODUTO_ID, vendaItem.getProdutoId());
            contentValues.put(VendaItem.COLUNA_QUANTIDADE, vendaItem.getQuantidade());
            contentValues.put(VendaItem.COLUNA_PRECO, vendaItem.getPreco());

            rowsAffected = db.update(VendaItem.TABLE_NAME, contentValues, VendaItem.COLUNA_ID + " = ?",
                    new String[]{vendaItem.getId()});
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
            rowsDeleted = db.delete(VendaItem.TABLE_NAME, VendaItem.COLUNA_ID + " = ?",
                    new String[]{id});
        } finally {
            Close();
        }

        return rowsDeleted;
    }

    // Select all VendaItems
    public List<VendaItem> selectAll() {
        List<VendaItem> vendaItems = new ArrayList<>();
        try {
            Open();
            Cursor cursor = db.query(VendaItem.TABLE_NAME, colunas, null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    VendaItem vendaItem = new VendaItem();
                    vendaItem.setId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_ID)));
                    vendaItem.setVendaId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_VENDA_ID)));
                    vendaItem.setProdutoId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRODUTO_ID)));
                    vendaItem.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_QUANTIDADE)));
                    vendaItem.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO)));
                    vendaItems.add(vendaItem);
                }
                cursor.close();
            }
        } finally {
            Close();
        }

        return vendaItems;
    }
}
