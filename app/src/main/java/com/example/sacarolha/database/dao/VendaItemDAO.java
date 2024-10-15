package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.VendaItem;
import com.example.sacarolha.util.model.MonthlyReport;

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
            contentValues.put(VendaItem.COLUNA_PRECO_TOTAL, vendaItem.getPrecoTotal());

            insertRows = db.insert(VendaItem.TABLE_NAME, null, contentValues);
        } finally {
            Close();
        }

        return insertRows;
    }

    public MonthlyReport getMonthlyReport(int month, int year) {
        MonthlyReport report = new MonthlyReport();
        Cursor cursor = null;
        try {
            Open();

            // SQL query to sum quantities and total prices for the given month and year
            String query = "SELECT SUM(" + VendaItem.COLUNA_QUANTIDADE + ") as totalQuantity, " +
                    "SUM(" + VendaItem.COLUNA_PRECO_TOTAL + ") as totalRevenue " +
                    "FROM " + VendaItem.TABLE_NAME + " vi " +
                    "INNER JOIN " + Venda.TABLE_NAME + " v " +
                    "ON vi." + VendaItem.COLUNA_VENDA_ID + " = v." + Venda.COLUNA_ID + " " +
                    "WHERE strftime('%m', v." + Venda.COLUNA_DATA + ") = ? " +
                    "AND strftime('%Y', v." + Venda.COLUNA_DATA + ") = ?";

            // Format the month and year to two-digit format for the query
            String[] selectionArgs = {String.format("%02d", month), String.valueOf(year)};

            // Execute the query
            cursor = db.rawQuery(query, selectionArgs);

            // Process the result
            if (cursor != null && cursor.moveToFirst()) {
                int totalQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuantity"));
                double totalRevenue = cursor.getDouble(cursor.getColumnIndexOrThrow("totalRevenue"));

                // Set the values in the report object
                report.setTotalQuantity(totalQuantity);
                report.setTotalRevenue(totalRevenue);
            }
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            Close();
        }

        return report; // Return the report object
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
                vendaItem.setPrecoTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO_TOTAL)));
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
            String selection = VendaItem.COLUNA_VENDA_ID + " = ?";
            String[] selectionArgs = {id};

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
                    vendaItem.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow(VendaItem.COLUNA_PRECO_TOTAL)));
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
