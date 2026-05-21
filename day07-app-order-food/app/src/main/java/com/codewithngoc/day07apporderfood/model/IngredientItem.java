package com.codewithngoc.day07apporderfood.model;

import java.util.List;

public class IngredientItem {
    private final String name;
    private final String amount;
    private final String emoji;
    private final int backgroundColorRes;
    private final List<String> tags;

    public IngredientItem(String name, String amount, String emoji, int backgroundColorRes, List<String> tags) {
        this.name = name;
        this.amount = amount;
        this.emoji = emoji;
        this.backgroundColorRes = backgroundColorRes;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getBackgroundColorRes() {
        return backgroundColorRes;
    }

    public List<String> getTags() {
        return tags;
    }
}
