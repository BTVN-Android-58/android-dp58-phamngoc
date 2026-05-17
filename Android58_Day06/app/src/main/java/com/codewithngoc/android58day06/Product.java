package com.codewithngoc.android58day06;

public class Product {
    private int id;
    private String name;
    private double price;
    private int imageResId;
    private float rating;
    private int quantity;
    private boolean isFavorite;

    public Product(int id, String name, double price, int imageResId, float rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.rating = rating;
        this.quantity = 0;
        this.isFavorite = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public float getRating() { return rating; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}