package com.example.syntax.alcjournalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.syntax.alcjournalapp.database.AppDatabase;
import com.example.syntax.alcjournalapp.database.NoteEntry;

public class AddNoteViewModel extends ViewModel {
    private LiveData<NoteEntry> note;

    public AddNoteViewModel(AppDatabase database, int noteId) {
        note = database.noteDao().getNoteById(noteId);
    }
    public LiveData<NoteEntry> getNote() {
        return note;
    }
}
