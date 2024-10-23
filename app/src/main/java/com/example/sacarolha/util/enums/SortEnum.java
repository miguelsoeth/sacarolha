package com.example.sacarolha.util.enums;

import android.content.Context;

import com.example.sacarolha.R;

public enum SortEnum {
    sort_mais_vendidos,
    sort_menos_vendidos,
    sort_maior_receita,
    sort_menor_receita;


    SortEnum() {

    }

    public String getDescricao(Context context) {
        int resId = context.getResources().getIdentifier(this.name().toLowerCase(), "string", context.getPackageName());
        return context.getString(resId);
    }

    public static int getPosition(Context context, String description) {
        for (int i = 0; i < SortEnum.values().length; i++) {
            if (SortEnum.values()[i].getDescricao(context).equals(description)) {
                return i;
            }
        }
        return -1;
    }

    public static String getResFromString(String tipo, Context context) {
        try {
            int pos = SortEnum.getPosition(context, tipo);
            String resString = SortEnum.values()[pos].name();
            int resId = context.getResources().getIdentifier(resString, "string", context.getPackageName());
            return context.getString(resId);
        }
        catch (Exception ex) {
            return context.getString(R.string.error);
        }
    }
}
