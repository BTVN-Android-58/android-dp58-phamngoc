package com.codewithngoc.day08apptakenote.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes " +
            "WHERE (:query = '' OR title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR tag LIKE '%' || :query || '%') " +
            "AND (:tag = '' OR tag = :tag) " +
            "ORDER BY pinned DESC, updatedAt DESC")
    List<NoteEntity> getNotes(String query, String tag);

    @Query("SELECT DISTINCT tag FROM notes WHERE tag != '' ORDER BY tag COLLATE NOCASE ASC")
    List<String> getTags();

    @Insert
    long insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    @Query("SELECT COUNT(*) FROM notes")
    int countNotes();
}
