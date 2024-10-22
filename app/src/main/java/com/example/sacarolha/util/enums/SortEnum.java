package com.example.sacarolha.util.enums;

import android.content.Context;

public enum SortEnum {
    MAIS_VENDIDOS("sort_mais_vendidos"),
    MENOS_VENDIDOS("sort_menos_vendidos"),
    MAIOR_RECEITA("sort_maior_receita"),
    MENOR_RECEITA("sort_menor_receita");

    private final String resourceName;

    SortEnum(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescricao(Context context) {
        int resId = context.getResources().getIdentifier(resourceName, "string", context.getPackageName());
        return context.getString(resId);
    }

    public static String[] getNameArray(Context context) {
        String[] names = new String[SortEnum.values().length];
        for (int i = 0; i < SortEnum.values().length; i++) {
            names[i] = SortEnum.values()[i].getDescricao(context);
        }
        return names;
    }

    public static int getPosition(Context context, String description) {
        for (int i = 0; i < SortEnum.values().length; i++) {
            if (SortEnum.values()[i].getDescricao(context).equals(description)) {
                return i;
            }
        }
        return -1;
    }
}
