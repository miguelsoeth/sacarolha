package com.example.sacarolha.util.enums;

public enum TiposVinhoEnum {
    NONE("Selecione o estado"),
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

    public String getDescricao() {
        return descricao;
    }
}
