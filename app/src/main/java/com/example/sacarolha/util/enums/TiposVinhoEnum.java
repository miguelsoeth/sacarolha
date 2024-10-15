package com.example.sacarolha.util.enums;

public enum TiposVinhoEnum {
    NONE("Selecione o tipo"),
    TINTO("Vinho Tinto"),
    BRANCO("Vinho Branco"),
    ROSE("Vinho Rosé"),
    ORGANICO("Vinho Orgânico");

    private final String descricao;

    TiposVinhoEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static String[] getNameArray() {
        String[] names = new String[TiposVinhoEnum.values().length];
        for (int i = 0; i < TiposVinhoEnum.values().length; i++) {
            names[i] = TiposVinhoEnum.values()[i].descricao;
        }
        return names;
    }

    public static int getPosition(String state) {
        for (int i = 0; i < TiposVinhoEnum.values().length; i++) {
            if (TiposVinhoEnum.values()[i].descricao.equals(state)) {
                return i;
            }
        }
        return -1;
    }
}
