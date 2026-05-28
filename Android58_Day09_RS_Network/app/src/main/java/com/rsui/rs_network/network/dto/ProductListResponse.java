package com.rsui.rs_network.network.dto;

import com.google.gson.annotations.SerializedName;
import com.rsui.rs_network.model.Product;

import java.util.List;

public class ProductListResponse {
    @SerializedName("products")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }
}
