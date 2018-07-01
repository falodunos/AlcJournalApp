package com.example.syntax.alcjournalapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.syntax.alcjournalapp.database.AppDatabase;

public class AddNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mNoteId;

    AddNoteViewModelFactory(AppDatabase database, int noteId) {
        mDb = database;
        mNoteId = noteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddNoteViewModel(mDb, mNoteId);
    }
}
