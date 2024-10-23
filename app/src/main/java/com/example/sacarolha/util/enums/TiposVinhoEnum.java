package com.example.sacarolha.util.enums;

import android.content.Context;

import com.example.sacarolha.R;

public enum TiposVinhoEnum {
    wine_type_none,
    wine_type_tinto,
    wine_type_branco,
    wine_type_rose,
    wine_type_organico;

    TiposVinhoEnum() {
    }

    public String getDescricao(Context context) {
        int stringResId = context.getResources().getIdentifier(this.name().toLowerCase(), "string", context.getPackageName());
        return context.getString(stringResId);
    }

    public static int getPosition(Context context, String descricao) {
        for (int i = 0; i < TiposVinhoEnum.values().length; i++) {
            if (TiposVinhoEnum.values()[i].getDescricao(context).equals(descricao)) {
                return i;
            }
        }
        return -1;
    }

    public static String getResFromString(String tipo, Context context) {
        try {
            int pos = TiposVinhoEnum.getPosition(context, tipo);
            String resString = TiposVinhoEnum.values()[pos].name();
            int resId = context.getResources().getIdentifier(resString, "string", context.getPackageName());
            return context.getString(resId);
        }
        catch (Exception ex) {
            return context.getString(R.string.error);
        }
    }
}
