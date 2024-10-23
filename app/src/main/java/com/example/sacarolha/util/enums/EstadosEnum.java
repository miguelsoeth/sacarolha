package com.example.sacarolha.util.enums;

import android.content.Context;

public enum EstadosEnum {
    state_none("state_none"),
    state_ac("state_ac"),
    state_al("state_al"),
    state_ap("state_ap"),
    state_am("state_am"),
    state_ba("state_ba"),
    state_ce("state_ce"),
    state_df("state_df"),
    state_es("state_es"),
    state_go("state_go"),
    state_ma("state_ma"),
    state_mt("state_mt"),
    state_ms("state_ms"),
    state_mg("state_mg"),
    state_pa("state_pa"),
    state_pb("state_pb"),
    state_pr("state_pr"),
    state_pe("state_pe"),
    state_pi("state_pi"),
    state_rj("state_rj"),
    state_rn("state_rn"),
    state_rs("state_rs"),
    state_ro("state_ro"),
    state_rr("state_rr"),
    state_sc("state_sc"),
    state_sp("state_sp"),
    state_se("state_se"),
    state_to("state_to");
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
