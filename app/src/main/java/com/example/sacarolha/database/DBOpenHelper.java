package com.example.sacarolha.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.User;
import com.example.sacarolha.database.model.Venda;
import com.example.sacarolha.database.model.VendaItem;
import com.example.sacarolha.database.model.Vinho;

public class DBOpenHelper extends SQLiteOpenHelper {


    private static final String
    DATABASE_NAME = "sqlite.db";

    private static final int
    DATABASE_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Vinho.CREATE_TABLE);
        db.execSQL(Cliente.CREATE_TABLE);
        db.execSQL(Venda.CREATE_TABLE);
        db.execSQL(VendaItem.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
