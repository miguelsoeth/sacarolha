package com.example.sacarolha.database.model;

public class Estado {

    public static final String TABLE_NAME = "tb_estados";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_UF = "uf",
            COLUNA_NOME = "nome";

    public static final String
            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Auto-incrementing primary key
            COLUNA_UF + " TEXT NOT NULL, " +
            COLUNA_NOME + " TEXT NOT NULL " +
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private int id;  // Integer for primary key
    private String uf;
    private String nome;

    // Constructors
    public Estado() {
    }

    public Estado(String uf, String nome) {
        this.uf = uf;
        this.nome = nome;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
