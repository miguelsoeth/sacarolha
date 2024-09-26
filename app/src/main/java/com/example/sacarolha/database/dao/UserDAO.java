package com.example.sacarolha.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sacarolha.util.handlers.PasswordHandler;
import com.example.sacarolha.util.handlers.StringHandler;
import com.example.sacarolha.database.DBOpenHelper;
import com.example.sacarolha.database.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstrataDAO {
    private final String[] colunas = {
            User.COLUNA_ID,
            User.COLUNA_NOME,
            User.COLUNA_USUARIO,
            User.COLUNA_SENHA
    };

    public UserDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    public long insert(User u) {

        try {
            u.setSenha(PasswordHandler.hashPassword(u.getSenha()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        u.setNome(StringHandler.capitalize(u.getNome()));

        long insertRows = 0;
        try {
            Open();

            ContentValues contentValues = new ContentValues();
            contentValues.put(User.COLUNA_ID, u.getId());
            contentValues.put(User.COLUNA_NOME, u.getNome());
            contentValues.put(User.COLUNA_USUARIO, u.getUsuario());
            contentValues.put(User.COLUNA_SENHA, u.getSenha());

            insertRows = db.insert(User.TABLE_NAME, null, contentValues);
        }
        finally {
            Close();
        }

        return insertRows;

    }

    public User getUserByUsername(String username) {

        User user = null;
        try {
            Open();

            String selection = User.COLUNA_USUARIO + " = ? ";
            String[] selectionArgs = { username };

            Cursor cursor = db.query(User.TABLE_NAME, colunas, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_ID)));
                user.setNome(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_NOME)));
                user.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_SENHA)));
                user.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_USUARIO)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return user;
    }

    public User selectById(long id) {
        User user = null;
        try {
            Open();
            Cursor cursor = db.query(User.TABLE_NAME, colunas, User.COLUNA_ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_ID)));
                user.setNome(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_NOME)));
                user.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_SENHA)));
                user.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_USUARIO)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return user;
    }

    public int update(User u) {
        int rowsAffected = 0;
        try {
            Open();
            ContentValues contentValues = new ContentValues();
            contentValues.put(User.COLUNA_NOME, u.getNome());
            contentValues.put(User.COLUNA_USUARIO, u.getUsuario());
            contentValues.put(User.COLUNA_SENHA, u.getSenha());

            rowsAffected = db.update(User.TABLE_NAME, contentValues, User.COLUNA_ID + " = ?",
                    new String[]{String.valueOf(u.getId())});
        } finally {
            Close();
        }
        return rowsAffected;
    }

    public int delete(long id) {
        int rowsDeleted = 0;
        try {
            Open();
            rowsDeleted = db.delete(User.TABLE_NAME, User.COLUNA_ID + " = ?",
                    new String[]{String.valueOf(id)});
        } finally {
            Close();
        }
        return rowsDeleted;
    }

    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        try {
            Open();
            Cursor cursor = db.query(User.TABLE_NAME, colunas, null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    User user = new User();
                    user.setId(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_ID)));
                    user.setNome(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_NOME)));
                    user.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_USUARIO)));
                    user.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUNA_SENHA)));
                    users.add(user);
                }
                cursor.close();
            }
        } finally {
            Close();
        }
        return users;
    }

}
