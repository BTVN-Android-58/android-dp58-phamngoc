package com.rsui.rs_network.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private int id;

    @SerializedName(value = "name", alternate = {"title"})
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName(value = "product_image", alternate = {"image", "thumbnail"})
    private String productImage;

    public int getId() {
        return id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public double getPrice() {
        return price;
    }

    public String getProductImage() {
        return productImage;
    }
}
