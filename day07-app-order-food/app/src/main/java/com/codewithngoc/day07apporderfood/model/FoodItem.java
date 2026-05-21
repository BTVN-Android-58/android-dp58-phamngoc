package com.codewithngoc.day07apporderfood.model;

import java.util.List;

public class FoodItem {
    private final int id;
    private final String title;
    private final String category;
    private final int durationMinutes;
    private final double rating;
    private final String description;
    private final String difficulty;
    private final int servings;
    private final String thumbnailEmoji;
    private final String heroEmoji;
    private final int thumbnailBackgroundColorRes;
    private final List<IngredientItem> ingredients;
    private boolean favorite;

    public FoodItem(
            int id,
            String title,
            String category,
            int durationMinutes,
            double rating,
            String description,
            String difficulty,
            int servings,
            String thumbnailEmoji,
            String heroEmoji,
            int thumbnailBackgroundColorRes,
            List<IngredientItem> ingredients,
            boolean favorite
    ) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.description = description;
        this.difficulty = difficulty;
        this.servings = servings;
        this.thumbnailEmoji = thumbnailEmoji;
        this.heroEmoji = heroEmoji;
        this.thumbnailBackgroundColorRes = thumbnailBackgroundColorRes;
        this.ingredients = ingredients;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getServings() {
        return servings;
    }

    public String getThumbnailEmoji() {
        return thumbnailEmoji;
    }

    public String getHeroEmoji() {
        return heroEmoji;
    }

    public int getThumbnailBackgroundColorRes() {
        return thumbnailBackgroundColorRes;
    }

    public List<IngredientItem> getIngredients() {
        return ingredients;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
