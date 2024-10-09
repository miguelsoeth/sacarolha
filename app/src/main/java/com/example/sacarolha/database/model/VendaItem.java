package com.example.sacarolha.database.model;

import java.io.Serializable;
import java.util.UUID;

public class VendaItem {

    public static final String TABLE_NAME = "tb_venda_items";

    public static final String
            COLUNA_ID = "_id",  // Sale item ID (Primary key)
            COLUNA_VENDA_ID = "venda_id",  // Foreign key to Venda
            COLUNA_PRODUTO_ID = "produto_id",  // Foreign key to Produto
            COLUNA_QUANTIDADE = "quantidade",  // Item quantity
            COLUNA_PRECO = "preco",
            COLUNA_PRECO_TOTAL = "preco_total"; // Item price

    public static final String
            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUNA_ID + " TEXT PRIMARY KEY, " +
            COLUNA_VENDA_ID + " TEXT NOT NULL, " +  // Foreign key
            COLUNA_PRODUTO_ID + " TEXT NOT NULL, " +  // Foreign key
            COLUNA_QUANTIDADE + " INTEGER NOT NULL, " +
            COLUNA_PRECO + " REAL NOT NULL, " +
            COLUNA_PRECO_TOTAL + " REAL NOT NULL, " +
            "FOREIGN KEY (" + COLUNA_VENDA_ID + ") REFERENCES tb_venda(_id) ON DELETE CASCADE, " +
            "FOREIGN KEY (" + COLUNA_PRODUTO_ID + ") REFERENCES tb_produto(_id) ON DELETE CASCADE" +
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private String id;  // UUID as a String
    private String vendaId;  // Foreign key to Venda
    private String produtoId;  // Foreign key to Produto
    private int quantidade;
    private double preco;
    private double precoTotal;

    // Constructor to automatically generate UUID
    public VendaItem() {
        this.id = UUID.randomUUID().toString();
    }

    public VendaItem(String vendaId, String produtoId, int quantidade, double preco, double precoTotal) {
        this.id = UUID.randomUUID().toString();
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.preco = preco;
        this.precoTotal = precoTotal;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendaId() {
        return vendaId;
    }

    public void setVendaId(String vendaId) {
        this.vendaId = vendaId;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }
}
