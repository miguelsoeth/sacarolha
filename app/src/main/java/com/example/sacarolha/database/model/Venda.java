package com.example.sacarolha.database.model;

import java.io.Serializable;
import java.util.UUID;

public class Venda {

    public static final String TABLE_NAME = "tb_vendas";

    public static final String
            COLUNA_ID = "_id",  // Sale ID
            COLUNA_DATA = "data",  // Date of sale
            COLUNA_TOTAL = "total",  // Total price of the sale
            COLUNA_CLIENTE_ID = "cliente_id",  // Foreign key to the client
            COLUNA_USER_ID = "user_id";  // Foreign key to the seller (user)

    public static final String
            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUNA_ID + " TEXT PRIMARY KEY, " +  // Store UUID as TEXT
            COLUNA_DATA + " TEXT NOT NULL, " +  // Date of sale is mandatory
            COLUNA_TOTAL + " REAL NOT NULL, " +  // Total price of the sale
            COLUNA_CLIENTE_ID + " TEXT, " +  // Foreign key to the client (can be null)
            COLUNA_USER_ID + " TEXT, " +  // Foreign key to the user (seller)
            "FOREIGN KEY (" + COLUNA_CLIENTE_ID + ") REFERENCES " + Cliente.TABLE_NAME + "(" + Cliente.COLUNA_ID + ") ON DELETE SET NULL, " +  // If client is deleted, set to NULL
            "FOREIGN KEY (" + COLUNA_USER_ID + ") REFERENCES " + User.TABLE_NAME + "(" + User.COLUNA_ID + ") ON DELETE CASCADE" +  // If user is deleted, sale is deleted
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private String id;  // UUID as a String
    private String data;  // Date of sale
    private double total;  // Total price of the sale
    private String clienteId;  // Foreign key to Cliente
    private String userId;  // Foreign key to User

    // Constructor to automatically generate UUID
    public Venda() {
        this.id = UUID.randomUUID().toString();
    }

    public Venda(String id, String data, double total, String clienteId, String userId) {
        this.id = id;
        this.data = data;
        this.total = total;
        this.clienteId = clienteId;
        this.userId = userId;
    }

    public Venda(String data, double total, String clienteId, String userId) {
        this.id = UUID.randomUUID().toString();
        this.data = data;
        this.total = total;
        this.clienteId = clienteId;
        this.userId = userId;
    }

    public Venda(String data, double total, String userId) {
        this.id = UUID.randomUUID().toString();
        this.data = data;
        this.total = total;
        this.clienteId = null;
        this.userId = userId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
