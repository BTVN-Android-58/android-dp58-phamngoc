package com.codewithngoc.day05pinterestscreen.model;

public class PinModel {
    private int imageResId;
    private String title;
    private String authorName;
    private int avatarResId;
    private int imageHeight; // for staggered grid varying heights

    public PinModel(int imageResId, String title, String authorName, int avatarResId, int imageHeight) {
        this.imageResId = imageResId;
        this.title = title;
        this.authorName = authorName;
        this.avatarResId = avatarResId;
        this.imageHeight = imageHeight;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}