package com.example.sacarolha.util.enums;

import android.content.Context;

public enum TiposVinhoEnum {
    wine_type_none("wine_type_none"),
    wine_type_tinto("wine_type_tinto"),
    wine_type_branco("wine_type_branco"),
    wine_type_rose("wine_type_rose"),
    wine_type_organico("wine_type_organico");

    private final String resourceName;

    TiposVinhoEnum(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescricao(Context context) {
        int resId = context.getResources().getIdentifier(resourceName, "string", context.getPackageName());
        return context.getString(resId);
    }

    public static String[] getNameArray(Context context) {
        String[] names = new String[TiposVinhoEnum.values().length];
        for (int i = 0; i < TiposVinhoEnum.values().length; i++) {
            names[i] = TiposVinhoEnum.values()[i].getDescricao(context);
        }
        return names;
    }

    public static int getPosition(Context context, String descricao) {
        for (int i = 0; i < TiposVinhoEnum.values().length; i++) {
            if (TiposVinhoEnum.values()[i].getDescricao(context).equals(descricao)) {
                return i;
            }
        }
        return -1;
    }
}
