package com.example.sacarolha.util.handlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.sacarolha.LoginActivity;
import com.example.sacarolha.util.Shared;
import com.example.sacarolha.util.model.Carrinho;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarrinhoHandler {

    private SharedPreferences preferences;
    private Gson gson;
    private String USER_CART_ITENS;
    private String USER_CART_TOTAL;

    public CarrinhoHandler(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userId = preferences.getString(Shared.KEY_USER_ID, "");
        USER_CART_ITENS = String.format("%s_CART", userId);
        USER_CART_TOTAL = String.format("%s_TOTAL", userId);

        gson = new Gson();
    }

    public void RemoverDoCarrinho(Carrinho item) {
        List<Carrinho> carrinho = LerCarrinho();

        for (Carrinho cartItem : carrinho) {
            if (Objects.equals(cartItem.getId(), item.getId())) {
                carrinho.remove(cartItem);
                SalvarCarrinho(carrinho);
                break;
            }
        }
    }

    public void SalvarCarrinho(List<Carrinho> carrinho) {
        String json = gson.toJson(carrinho);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(USER_CART_ITENS, json);
        edit.apply();
    }

    public List<Carrinho> LerCarrinho() {
        String json = preferences.getString(USER_CART_ITENS, null);

        if (json != null) {
            Type type = new TypeToken<ArrayList<Carrinho>>() {}.getType();
            return gson.fromJson(json, type);
        }

        return new ArrayList<Carrinho>();
    }

    public String ValorVazio() {
        return "R$ 0,00";
    }

    public void LimparCarrinho() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(USER_CART_TOTAL);
        edit.remove(USER_CART_ITENS);
        edit.apply();
    }
}
