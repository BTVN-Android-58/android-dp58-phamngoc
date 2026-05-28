package com.rsui.rs_network.model;


import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String name;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("email")
    private String email;

    @SerializedName("avatar")
    private String avatarUrl;

    // Constructors
    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return displayName; }
    public void setName(String name) { this.displayName = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public com.rsui.rs_network.storage.database.entities.User toEntity() {
        return new com.rsui.rs_network.storage.database.entities.User(id, name, email, displayName, avatarUrl);
    }
}

