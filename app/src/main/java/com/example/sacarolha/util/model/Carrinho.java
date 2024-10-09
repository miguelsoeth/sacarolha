package com.example.sacarolha.util.model;

import com.example.sacarolha.database.model.Vinho;

import java.io.Serializable;

public class Carrinho implements Serializable {

    private String id;  // UUID as a String
    private String nome;
    private String tipo;
    private Integer safra;  // Nullable, for non-vintage wines
    private double preco;
    private int estoque;
    private String codigo;
    private String userId;
    private int quantidade;

    public Carrinho(Vinho vinho, int quantidade) {
        this.id = vinho.getId();
        this.nome = vinho.getNome();
        this.tipo = vinho.getTipo();
        this.safra = vinho.getSafra();
        this.preco = vinho.getPreco();
        this.estoque = vinho.getEstoque();
        this.codigo = vinho.getCodigo();
        this.userId = vinho.getUserId();
        this.quantidade = quantidade;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
