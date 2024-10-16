package com.example.sacarolha.util.enums;

public enum SortEnum {
    MAIS_VENDIDOS("Mais Vendidos"),
    MENOS_VENDIDOS("Menos Vendidos"),
    MAIOR_RECEITA("Maior Receita"),
    MENOR_RECEITA("Menos Receita");

    private final String descricao;

    SortEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static String[] getNameArray() {
        String[] names = new String[SortEnum.values().length];
        for (int i = 0; i < SortEnum.values().length; i++) {
            names[i] = SortEnum.values()[i].descricao;
        }
        return names;
    }

    public static int getPosition(String state) {
        for (int i = 0; i < SortEnum.values().length; i++) {
            if (SortEnum.values()[i].descricao.equals(state)) {
                return i;
            }
        }
        return -1;
    }
}

