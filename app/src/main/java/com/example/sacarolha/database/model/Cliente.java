package com.example.sacarolha.database.model;

import java.util.UUID;

public class Cliente {

    public static final String TABLE_NAME = "tb_cliente";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_NOME = "nome",
            COLUNA_DOCUMENTO = "documento",
            COLUNA_TELEFONE = "telefone",
            COLUNA_EMAIL = "email",
            COLUNA_LOGRADOURO = "logradouro",
            COLUNA_NUMERO = "numero",
            COLUNA_CIDADE = "cidade",
            COLUNA_ESTADO = "estado";

    public static final String
            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUNA_ID + " TEXT PRIMARY KEY, " +  // Store UUID as TEXT
            COLUNA_NOME + " TEXT NOT NULL, " +
            COLUNA_DOCUMENTO + " TEXT NOT NULL, " +
            COLUNA_TELEFONE + " TEXT NOT NULL, " +
            COLUNA_EMAIL + " TEXT, " +  // Email is optional
            COLUNA_LOGRADOURO + " TEXT NOT NULL, " +
            COLUNA_NUMERO + " TEXT NOT NULL, " +
            COLUNA_CIDADE + " TEXT NOT NULL, " +
            COLUNA_ESTADO + " TEXT NOT NULL " +
            ");";

    public static final String
            DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private String id;  // UUID as a String
    private String nome;
    private String documento;
    private String telefone;
    private String email;  // Optional field
    private String logradouro;
    private String numero;
    private String cidade;
    private String estado;

    // Constructor to automatically generate UUID
    public Cliente() {
        this.id = UUID.randomUUID().toString();
    }

    public Cliente(String nome, String documento, String telefone, String logradouro, String numero, String cidade, String estado, String email) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.email = email;  // Optional
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
