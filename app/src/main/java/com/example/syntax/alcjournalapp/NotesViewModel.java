package com.example.syntax.alcjournalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.syntax.alcjournalapp.database.AppDatabase;
import com.example.syntax.alcjournalapp.database.NoteEntry;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private LiveData <List<NoteEntry>> notes;

    public NotesViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        notes = database.noteDao().loadAllNotes();
    }

    public LiveData<List<NoteEntry>> getNotes() {
        return notes;
    }
}
