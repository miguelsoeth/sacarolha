package com.example.sacarolha.util.model;

public class ProdutoTotal {
    private String produtoNome;
    private int totalQuantidade;
    private double precoUnitario;
    private double totalGasto;

    // Getters and Setters
    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getTotalQuantidade() {
        return totalQuantidade;
    }

    public void setTotalQuantidade(int totalQuantidade) {
        this.totalQuantidade = totalQuantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(double totalGasto) {
        this.totalGasto = totalGasto;
    }
}