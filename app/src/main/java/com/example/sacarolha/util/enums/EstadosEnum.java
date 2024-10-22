package com.example.sacarolha.util.enums;

import android.content.Context;

public enum EstadosEnum {
    NONE("state_none"),
    AC("state_ac"),
    AL("state_al"),
    AP("state_ap"),
    AM("state_am"),
    BA("state_ba"),
    CE("state_ce"),
    DF("state_df"),
    ES("state_es"),
    GO("state_go"),
    MA("state_ma"),
    MT("state_mt"),
    MS("state_ms"),
    MG("state_mg"),
    PA("state_pa"),
    PB("state_pb"),
    PR("state_pr"),
    PE("state_pe"),
    PI("state_pi"),
    RJ("state_rj"),
    RN("state_rn"),
    RS("state_rs"),
    RO("state_ro"),
    RR("state_rr"),
    SC("state_sc"),
    SP("state_sp"),
    SE("state_se"),
    TO("state_to");

    private final String resourceName;

    EstadosEnum(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescricao(Context context) {
        int resId = context.getResources().getIdentifier(resourceName, "string", context.getPackageName());
        return context.getString(resId);
    }

    public static String[] getNameArray(Context context) {
        String[] names = new String[EstadosEnum.values().length];
        for (int i = 0; i < EstadosEnum.values().length; i++) {
            names[i] = EstadosEnum.values()[i].getDescricao(context);
        }
        return names;
    }

    public static int getPosition(Context context, String state) {
        for (int i = 0; i < EstadosEnum.values().length; i++) {
            if (EstadosEnum.values()[i].getDescricao(context).equals(state)) {
                return i;
            }
        }
        return -1;
    }
}
