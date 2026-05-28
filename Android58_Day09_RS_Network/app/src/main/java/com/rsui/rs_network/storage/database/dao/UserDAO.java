package com.rsui.rs_network.storage.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rsui.rs_network.storage.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE user_name LIKE :username LIMIT 1")
    User findByName(String username);

    @Query("SELECT * FROM users WHERE email LIKE :email LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * FROM users WHERE display_name LIKE :displayName")
    User findByDisplayName(String displayName);


    @Insert
    void insertAll(User... users);

    @Insert
    void insertUser(User user);

    @Delete
    void delete(User user);
}
