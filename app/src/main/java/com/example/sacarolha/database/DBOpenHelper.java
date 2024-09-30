package com.example.sacarolha.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sacarolha.database.model.Cidade;
import com.example.sacarolha.database.model.Cliente;
import com.example.sacarolha.database.model.Estado;
import com.example.sacarolha.database.model.User;
import com.example.sacarolha.database.model.Vinho;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBOpenHelper extends SQLiteOpenHelper {

    private final Context context;

    private static final String
    DATABASE_NAME = "sqlite.db";

    private static final int
    DATABASE_VERSION = 1;


    private final String
    DATABASE_PATH;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Vinho.CREATE_TABLE);
        db.execSQL(Cliente.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase openDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();

        if (!dbExists) {
            this.getWritableDatabase(); // Creates the empty database in the default system path
            this.close(); // Close the database connection
            try {
                copyDatabase();
                SQLiteDatabase db = this.openDatabase();
                this.onCreate(db);
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // Database does not exist yet
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open(DATABASE_NAME);
        OutputStream output = new FileOutputStream(DATABASE_PATH);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }
}
