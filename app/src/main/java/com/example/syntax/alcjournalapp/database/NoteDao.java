package com.example.syntax.alcjournalapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt")
    LiveData<List<NoteEntry>> loadAllNotes();

    @Insert
    void insertNote(NoteEntry noteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatenote(NoteEntry noteEntry);

    @Delete
    void deleteNote(NoteEntry noteEntry);

    @Query("SELECT * FROM notes WHERE id = :noteId")
    LiveData<NoteEntry> getNoteById(int noteId);
}
