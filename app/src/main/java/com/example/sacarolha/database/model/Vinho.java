package com.example.sacarolha.database.model;

import com.example.sacarolha.util.enums.TiposVinhoEnum;

import java.util.UUID;

public class Vinho {

    public static final String TABLE_NAME = "tb_vinho";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_NOME = "nome",
            COLUNA_TIPO = "tipo",
            COLUNA_SAFRA = "safra",
            COLUNA_PRECO = "preco",
            COLUNA_ESTOQUE = "estoque",
            COLUNA_USER_ID = "user_id";  // New column for foreign key

    public static final String
            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUNA_ID + " TEXT PRIMARY KEY, " +  // Store UUID as TEXT
            COLUNA_NOME + " TEXT NOT NULL, " +
            COLUNA_TIPO + " TEXT NOT NULL, " +
            COLUNA_SAFRA + " INTEGER, " +
            COLUNA_PRECO + " REAL NOT NULL, " +
            COLUNA_ESTOQUE + " INTEGER NOT NULL, " +
            COLUNA_USER_ID + " TEXT, " +
            "FOREIGN KEY (" + COLUNA_USER_ID + ") REFERENCES " + User.TABLE_NAME + "(" + User.COLUNA_ID + ") ON DELETE CASCADE" +
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private String id;  // UUID as a String
    private String nome;
    private String tipo;
    private Integer safra;  // Nullable, for non-vintage wines
    private double preco;
    private int estoque;
    private String userId;  // Foreign key to User

    // Constructor to automatically generate UUID
    public Vinho() {
        this.id = UUID.randomUUID().toString();
    }

    public Vinho(String nome, String tipo, Integer safra, double preco, int estoque, String userId) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.tipo = tipo;
        this.safra = safra;  // Can be null
        this.preco = preco;
        this.estoque = estoque;
        this.userId = userId;  // Set the foreign key
    }

    public Vinho(String id, String nome, String tipo, Integer safra, double preco, int estoque, String userId) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.safra = safra;  // Can be null
        this.preco = preco;
        this.estoque = estoque;
        this.userId = userId;  // Set the foreign key
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getSafra() {
        return safra;
    }

    public void setSafra(Integer safra) {
        this.safra = safra;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}