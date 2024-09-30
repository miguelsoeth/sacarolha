package com.example.sacarolha.database.model;

public class Cidade {

    public static final String TABLE_NAME = "tb_cidades";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_ESTADO = "estado",
            COLUNA_UF = "uf",
            COLUNA_NOME = "nome";

    public static final String
            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUNA_ESTADO + " INTEGER NOT NULL DEFAULT 0, " +
            COLUNA_UF + " TEXT NOT NULL DEFAULT '', " +
            COLUNA_NOME + " TEXT NOT NULL DEFAULT '' " +
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private int id;  // Unique identifier for the city
    private int estado;  // References the state (Estado)
    private String uf;  // State code (UF)
    private String nome;  // City name

    // Constructors
    public Cidade() {
    }

    public Cidade(int estado, String uf, String nome) {
        this.estado = estado;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
