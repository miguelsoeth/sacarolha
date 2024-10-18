package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.VendaItem;
import com.example.sacarolha.database.model.Vinho;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.model.VendaPorTipoVinho;

import java.util.ArrayList;
import java.util.List;

public class VendaItemDAO extends AbstrataDAO {
    private final String[] colunas = {
            VendaItem.COLUNA_ID,
            VendaItem.COLUNA_VENDA_ID,
            VendaItem.COLUNA_PRODUTO_ID,
            VendaItem.COLUNA_QUANTIDADE,
            VendaItem.COLUNA_PRECO,
            VendaItem.COLUNA_PRECO_TOTAL
    };

    String userId;

    public VendaItemDAO(Context context) {
        db_helper = new DBOpenHelper(context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        userId = preferences.getString(Shared.KEY_USER_ID, "");
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
            contentValues.put(VendaItem.COLUNA_PRECO_TOTAL, vendaItem.getPrecoTotal());
            contentValues.put(VendaItem.COLUNA_USER_ID, vendaItem.getUserId());

            insertRows = db.insert(VendaItem.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    public List<VendaPorTipoVinho> getVendasPorTipoDeVinho(int month, int year) {
        List<VendaPorTipoVinho> vendas = new ArrayList<>();
        Cursor cursor = null;
        try {
            Open();

            // SQL query to get sales data grouped by wine type
            String query = "SELECT " +
                    "p." + Vinho.COLUNA_TIPO + " AS tipo_vinho, " +
                    "SUM(iv." + VendaItem.COLUNA_QUANTIDADE + ") AS quantidade_vendida, " +
                    "SUM(iv." + VendaItem.COLUNA_PRECO_TOTAL + ") AS valor_total " +
                    "FROM " + VendaItem.TABLE_NAME + " iv " +
                    "JOIN " + Vinho.TABLE_NAME + " p ON iv." + VendaItem.COLUNA_PRODUTO_ID + " = p." + Vinho.COLUNA_ID + " " +
                    "JOIN " + Venda.TABLE_NAME + " pe ON iv." + VendaItem.COLUNA_VENDA_ID + " = pe." + Venda.COLUNA_ID + " " +
                    "WHERE strftime('%Y', pe." + Venda.COLUNA_DATA + ") = ? AND " +
                    "strftime('%m', pe." + Venda.COLUNA_DATA + ") = ? AND " +
                    "iv."+VendaItem.COLUNA_USER_ID + " = ? " +
                    "GROUP BY p." + Vinho.COLUNA_TIPO + " " +
                    "ORDER BY quantidade_vendida DESC;";

            // Format the year and month for the query
            String[] selectionArgs = {String.valueOf(year), String.format("%02d", month), userId};

            // Execute the query
            cursor = db.rawQuery(query, selectionArgs);

            // Process the results
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String tipoVinho = cursor.getString(cursor.getColumnIndexOrThrow("tipo_vinho"));
                    int quantidadeVendida = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade_vendida"));
                    double valorTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("valor_total"));

                    // Create a new VendaPorTipoVinho object to hold the results
                    VendaPorTipoVinho vendaPorTipo = new VendaPorTipoVinho(tipoVinho, quantidadeVendida, valorTotal);
                    vendas.add(vendaPorTipo);
                } while (cursor.moveToNext());
            }
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return vendas; // Return the list of sales by wine type
    }

    // Get VendaItem by ID
    public VendaItem selectById(String id) {
        VendaItem vendaItem = null;
        try {
            Open();
            String selection = VendaItem.COLUNA_ID + " = ? AND "+ VendaItem.COLUNA_USER_ID + " = ? ";
            String[] selectionArgs = {id, userId};

            Cursor cursor = db.query(VendaItem.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vendaItem = new VendaItem();
                vendaItem.setId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_ID)));
                vendaItem.setVendaId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_VENDA_ID)));
                vendaItem.setProdutoId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRODUTO_ID)));
                vendaItem.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_QUANTIDADE)));
                vendaItem.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO)));
                vendaItem.setPrecoTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO_TOTAL)));
                vendaItem.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_USER_ID)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return vendaItem;
    }

    public List<VendaItem> selectAllByVendaId(String id) {
        List<VendaItem> vendaItems = new ArrayList<>();
        Cursor cursor = null;
        try {
            Open();

            // Define the selection and arguments for the query
            String selection = VendaItem.COLUNA_VENDA_ID + " = ? AND "+ VendaItem.COLUNA_USER_ID + " = ? ";
            String[] selectionArgs = {id, userId};

            // Perform the query
            cursor = db.query(VendaItem.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            // If the cursor has rows, iterate over each row and add to the list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VendaItem vendaItem = new VendaItem();
                    vendaItem.setId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_ID)));
                    vendaItem.setVendaId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_VENDA_ID)));
                    vendaItem.setProdutoId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRODUTO_ID)));
                    vendaItem.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_QUANTIDADE)));
                    vendaItem.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO)));
                    vendaItem.setPrecoTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO_TOTAL)));
                    vendaItem.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_USER_ID)));

                    // Add the vendaItem to the list
                    vendaItems.add(vendaItem);
                } while (cursor.moveToNext()); // Move to the next row
            }
        } finally {
            // Close the cursor if it's not null
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return vendaItems; // Return the list of vendaItems
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
            contentValues.put(VendaItem.COLUNA_PRECO_TOTAL, vendaItem.getPrecoTotal());
            contentValues.put(VendaItem.COLUNA_USER_ID, vendaItem.getUserId());

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

            String selection = VendaItem.COLUNA_USER_ID + " = ?";
            String[] selectionArgs = {userId};

            Cursor cursor = db.query(VendaItem.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    VendaItem vendaItem = new VendaItem();
                    vendaItem.setId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_ID)));
                    vendaItem.setVendaId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_VENDA_ID)));
                    vendaItem.setProdutoId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRODUTO_ID)));
                    vendaItem.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_QUANTIDADE)));
                    vendaItem.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO)));
                    vendaItem.setPrecoTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO_TOTAL)));
                    vendaItem.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_USER_ID)));
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
