package com.example.sacarolha.database.dao;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.sacarolha.database.DBOpenHelper;

import java.io.IOException;

public abstract class AbstrataDAO {

    protected SQLiteDatabase db;
    protected DBOpenHelper db_helper;

    protected final void Open() {
        db = db_helper.openDatabase();
    }

    protected final void Close() {
        db_helper.close();
    }

}
