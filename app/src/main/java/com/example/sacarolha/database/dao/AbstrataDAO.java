package com.example.sacarolha.database.dao;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.sacarolha.database.DBOpenHelper;

public abstract class AbstrataDAO {

    protected SQLiteDatabase db;
    protected DBOpenHelper db_helper;

    protected final void Open() {
        db = db_helper.getWritableDatabase();
    }

    protected final void Close() {
        db_helper.close();
    }

}
