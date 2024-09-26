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
            COLUNA_ESTOQUE = "estoque";

    public static final String
            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUNA_ID + " TEXT PRIMARY KEY, " +  // Store UUID as TEXT
            COLUNA_NOME + " TEXT NOT NULL, " +
            COLUNA_TIPO + " TEXT NOT NULL, " +  // Using the enum's description as TEXT
            COLUNA_SAFRA + " INTEGER, " +  // Safra can be null for non-vintage wines
            COLUNA_PRECO + " REAL NOT NULL, " +  // Price as a floating-point value
            COLUNA_ESTOQUE + " INTEGER NOT NULL" +  // Available stock as an integer
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private String id;  // UUID as a String
    private String nome;
    private String tipo;
    private Integer safra;  // Nullable, for non-vintage wines
    private double preco;
    private int estoque;

    // Constructor to automatically generate UUID
    public Vinho() {
        this.id = UUID.randomUUID().toString();
    }

    public Vinho(String nome, String tipo, Integer safra, double preco, int estoque) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.tipo = tipo;
        this.safra = safra;  // Can be null
        this.preco = preco;
        this.estoque = estoque;
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
}
