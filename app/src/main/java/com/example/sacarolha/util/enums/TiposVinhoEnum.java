package com.example.sacarolha.util.enums;

public enum TiposVinhoEnum {
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
    VINHO_DO_PORTO("Vinho do Porto"),
    VINHO_MADEIRA("Vinho Madeira"),
    VINHO_MARSALA("Vinho Marsala"),
    VINHO_SHERRY("Vinho Sherry/Jerez"),
    VINHO_MOSCATEL("Vinho Moscatel"),
    VINHO_ICEWINE("Vinho Icewine"),
    VINHO_COLHEITA_TARDIA("Vinho de Colheita Tardia"),
    VINHO_ORGANICO("Vinho Orgânico"),
    VINHO_BIODINAMICO("Vinho Biodinâmico"),
    VINHO_NATURAL("Vinho Natural");

    private final String descricao;

    TiposVinhoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
