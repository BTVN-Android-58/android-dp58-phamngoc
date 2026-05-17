package com.codewithngoc.android58day06;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartPreferenceHelper {
    private static final String PREF_NAME = "cart_preferences";
    private static final String KEY_CART_ITEMS = "cart_items";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public CartPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Save entire cart items list using Gson
    public void saveCartItems(List<CartData> items) {
        String json = gson.toJson(items);
        sharedPreferences.edit().putString(KEY_CART_ITEMS, json).apply();
    }

    // Load cart items list using Gson
    public List<CartData> loadCartItems() {
        String json = sharedPreferences.getString(KEY_CART_ITEMS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<CartData>>() {}.getType();
        List<CartData> items = gson.fromJson(json, type);
        return items != null ? items : new ArrayList<>();
    }

    // Save quantity for a single product (quick update without saving entire list)
    public void saveQuantity(int productId, int quantity) {
        List<CartData> items = loadCartItems();
        boolean found = false;
        for (CartData item : items) {
            if (item.productId == productId) {
                item.quantity = quantity;
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(new CartData(productId, quantity, false));
        }
        saveCartItems(items);
    }

    // Save favorite for a single product
    public void saveFavorite(int productId, boolean isFavorite) {
        List<CartData> items = loadCartItems();
        boolean found = false;
        for (CartData item : items) {
            if (item.productId == productId) {
                item.isFavorite = isFavorite;
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(new CartData(productId, 0, isFavorite));
        }
        saveCartItems(items);
    }

    // Load quantity for a product
    public int loadQuantity(int productId) {
        List<CartData> items = loadCartItems();
        for (CartData item : items) {
            if (item.productId == productId) {
                return item.quantity;
            }
        }
        return 0;
    }

    // Load favorite for a product
    public boolean loadFavorite(int productId) {
        List<CartData> items = loadCartItems();
        for (CartData item : items) {
            if (item.productId == productId) {
                return item.isFavorite;
            }
        }
        return false;
    }

    // Clear all cart data
    public void clearCart() {
        sharedPreferences.edit().clear().apply();
    }

    // Inner class for Gson serialization
    public static class CartData {
        public int productId;
        public int quantity;
        public boolean isFavorite;

        public CartData(int productId, int quantity, boolean isFavorite) {
            this.productId = productId;
            this.quantity = quantity;
            this.isFavorite = isFavorite;
        }
    }
}