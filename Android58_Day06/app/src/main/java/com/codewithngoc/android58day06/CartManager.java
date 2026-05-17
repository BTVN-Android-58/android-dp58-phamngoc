package com.codewithngoc.android58day06;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Product> allProducts;
    private CartPreferenceHelper preferenceHelper;

    private CartManager() {
        allProducts = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void init(Context context) {
        if (preferenceHelper == null) {
            preferenceHelper = new CartPreferenceHelper(context);
        }
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<Product> products) {
        this.allProducts = products;
        // Load saved quantities and favorites from SharedPreferences
        if (preferenceHelper != null) {
            for (Product product : allProducts) {
                product.setQuantity(preferenceHelper.loadQuantity(product.getId()));
                product.setFavorite(preferenceHelper.loadFavorite(product.getId()));
            }
        }
    }

    public void saveProductQuantity(int productId, int quantity) {
        if (preferenceHelper != null) {
            preferenceHelper.saveQuantity(productId, quantity);
        }
    }

    public void saveProductFavorite(int productId, boolean isFavorite) {
        if (preferenceHelper != null) {
            preferenceHelper.saveFavorite(productId, isFavorite);
        }
    }

    public List<Product> getCartItems() {
        List<Product> cartItems = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getQuantity() > 0) {
                cartItems.add(product);
            }
        }
        return cartItems;
    }

    public double getSubTotal() {
        double subtotal = 0;
        for (Product product : allProducts) {
            subtotal += product.getQuantity() * product.getPrice();
        }
        return subtotal;
    }

    public double getTotal() {
        return getSubTotal();
    }

    public void clearCart() {
        if (preferenceHelper != null) {
            preferenceHelper.clearCart();
        }
        for (Product product : allProducts) {
            product.setQuantity(0);
        }
    }
}