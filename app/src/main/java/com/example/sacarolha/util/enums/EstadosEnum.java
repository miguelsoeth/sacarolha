package com.example.sacarolha.util.enums;

import android.content.Context;

import com.example.sacarolha.R;

public enum EstadosEnum {
    state_none,
    state_ac,
    state_al,
    state_ap,
    state_am,
    state_ba,
    state_ce,
    state_df,
    state_es,
    state_go,
    state_ma,
    state_mt,
    state_ms,
    state_mg,
    state_pa,
    state_pb,
    state_pr,
    state_pe,
    state_pi,
    state_rj,
    state_rn,
    state_rs,
    state_ro,
    state_rr,
    state_sc,
    state_sp,
    state_se,
    state_to;

    EstadosEnum() {

    }

    public String getDescricao(Context context) {
        int resId = context.getResources().getIdentifier(this.name().toLowerCase(), "string", context.getPackageName());
        return context.getString(resId);
    }

    public static int getPosition(Context context, String state) {
        for (int i = 0; i < EstadosEnum.values().length; i++) {
            if (EstadosEnum.values()[i].getDescricao(context).equals(state)) {
                return i;
            }
        }
        return -1;
    }

    public static String getResFromString(String tipo, Context context) {
        try {
            int pos = EstadosEnum.getPosition(context, tipo);
            String resString = EstadosEnum.values()[pos].name();
            int resId = context.getResources().getIdentifier(resString, "string", context.getPackageName());
            return context.getString(resId);
        }
        catch (Exception ex) {
            return context.getString(R.string.error);
        }
    }
}
