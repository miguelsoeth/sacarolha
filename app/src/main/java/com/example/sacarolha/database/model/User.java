package com.example.sacarolha.database.model;

import java.util.UUID;

public class User {

    public static final String TABLE_NAME = "tb_user";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_NOME = "nome",
            COLUNA_USUARIO = "usuario",
            COLUNA_SENHA = "senha";

    public static final String
            CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            "(" +
            COLUNA_ID+" TEXT PRIMARY KEY, " +  // Store UUID as TEXT
            COLUNA_NOME+" TEXT NOT NULL, " +
            COLUNA_USUARIO+" TEXT NOT NULL, " +
            COLUNA_SENHA+" TEXT NOT NULL " +
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME+";";

    private String id;  // UUID as a String
    private String nome;
    private String usuario;
    private String senha;

    // Constructor to automatically generate UUID
    public User() {
        this.id = UUID.randomUUID().toString(); // Generate random UUID
    }

    public User(String nome, String usuario, String senha) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
    }

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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
