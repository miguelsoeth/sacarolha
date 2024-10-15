package com.example.sacarolha.util.model;

public class VendaPorTipoVinho {
    private String tipoVinho;
    private int quantidadeVendida;
    private double valorTotal;

    public VendaPorTipoVinho(String tipoVinho, int quantidadeVendida, double valorTotal) {
        this.tipoVinho = tipoVinho;
        this.quantidadeVendida = quantidadeVendida;
        this.valorTotal = valorTotal;
    }

    // Getters and Setters
    public String getTipoVinho() {
        return tipoVinho;
    }

    public void setTipoVinho(String tipoVinho) {
        this.tipoVinho = tipoVinho;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
