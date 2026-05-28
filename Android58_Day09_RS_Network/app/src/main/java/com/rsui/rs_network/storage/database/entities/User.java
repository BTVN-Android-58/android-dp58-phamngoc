package com.rsui.rs_network.storage.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    // This is the constructor Room will use. Its parameters match the class fields.
    public User(int uid, String userName, String email, String displayName, String avatarUrl) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }

    // Keep your original constructor for convenience, but tell Room to ignore it.
    @Ignore
    public User(String name, String email) {
        this.email = email;
        this.displayName = name;
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "display_name")
    public String displayName;

    @ColumnInfo(name = "avatar")
    public String avatarUrl;



}

