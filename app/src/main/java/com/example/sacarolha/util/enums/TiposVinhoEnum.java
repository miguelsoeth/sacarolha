package com.example.sacarolha.util.enums;

public enum TiposVinhoEnum {
    NONE("Selecione o tipo"),
    TINTO("Vinho Tinto"),
    BRANCO("Vinho Branco"),
    ROSE("Vinho Rosé"),
    ESPUMANTE("Vinho Espumante"),
    SOBREMESA("Vinho de Sobremesa"),
    FORTIFICADO("Vinho Fortificado"),
    VERDE("Vinho Verde"),
    LARANJA("Vinho Laranja"),
    ESPUMANTE_NATURAL("Espumante Natural"),
    LAMBRUSCO("Vinho Lambrusco"),
    PROSECCO("Vinho Prosecco"),
    CHAMPAGNE("Champagne"),
    DO_PORTO("Vinho do Porto"),
    MADEIRA("Vinho Madeira"),
    MARSALA("Vinho Marsala"),
    SHERRY("Vinho Sherry/Jerez"),
    MOSCATEL("Vinho Moscatel"),
    ICEWINE("Vinho Icewine"),
    COLHEITA_TARDIA("Vinho de Colheita Tardia"),
    ORGANICO("Vinho Orgânico"),
    BIODINAMICO("Vinho Biodinâmico"),
    NATURAL("Vinho Natural");

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
