package com.example.syntax.alcjournalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syntax.alcjournalapp.database.AppDatabase;
import com.example.syntax.alcjournalapp.database.NoteEntry;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_NOTE_ID = "extraNoteId";
    public static final String INSTANCE_NOTE_ID = "instanceNoteId";
    public static final int DEFAULT_NOTE_ID = -1;
    private static final String TAG = AddNoteActivity.class.getSimpleName();
    private AppDatabase mDb;
    private int mNoteId = DEFAULT_NOTE_ID;
    private String author;

    private FirebaseAuth mAuth;

    EditText mEditTextNoteTitle;
    EditText mEditTextNoteDescription;
    EditText mEditTextNoteContent;
    EditText mEditTextNoteTags;

    TextView mTextViewAddNote;
    Button mAddNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mDb = AppDatabase.getsInstance(getApplicationContext()); // database instance

        mAuth = FirebaseAuth.getInstance(); // firebase auth instance

        if (mAuth.getCurrentUser() != null) {
            String displayName = mAuth.getCurrentUser().getDisplayName();
            String emailAddress = mAuth.getCurrentUser().getEmail();
            author = !TextUtils.isEmpty(displayName) ? displayName : emailAddress;
        }

        initViews();

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_NOTE_ID)){
            mNoteId = savedInstanceState.getInt(INSTANCE_NOTE_ID, DEFAULT_NOTE_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_NOTE_ID)) {
            mAddNoteButton.setText(R.string.update_button);
            mTextViewAddNote.setText(mAddNoteButton.getText().toString().toUpperCase());

            if (mNoteId == DEFAULT_NOTE_ID) {
                mNoteId = intent.getIntExtra(EXTRA_NOTE_ID, DEFAULT_NOTE_ID);

                AddNoteViewModelFactory factory = new AddNoteViewModelFactory(mDb, mNoteId);
                final AddNoteViewModel noteViewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);

                noteViewModel.getNote().observe(this, new Observer<NoteEntry>() {
                    @Override
                    public void onChanged(@Nullable NoteEntry noteEntry) {
                        noteViewModel.getNote().removeObserver(this);
                        populateUI(noteEntry);
                    }
                });
            }
        }

        mAddNoteButton.setOnClickListener(this);
    }

    /**
     *
     * @param noteEntry a given note instance to populate UI with
     */
    private void populateUI(NoteEntry noteEntry) {
        if (noteEntry == null) {
            return ;
        }
        mEditTextNoteTitle.setText(noteEntry.getTitle());
        mEditTextNoteDescription.setText(noteEntry.getDescription());
        mEditTextNoteContent.setText(noteEntry.getContent());
        mEditTextNoteTags.setText(noteEntry.getTags());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_NOTE_ID, mNoteId);
        super.onSaveInstanceState(outState);
    }


    private void initViews() {

        mTextViewAddNote = findViewById(R.id.textViewAddNoteLabel);
        mEditTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        mEditTextNoteDescription = findViewById(R.id.editTextNoteDescription);
        mEditTextNoteContent = findViewById(R.id.editTextNoteContent);
        mEditTextNoteTags = findViewById(R.id.editTextNoteTags);

        mAddNoteButton = findViewById(R.id.saveButton);
    }

    private boolean validateFormFields(String[] fieldValues) {
        boolean status = true;
        for (int i = 0; i < fieldValues.length; i++) {
            if (TextUtils.isEmpty((fieldValues[i]))) {
                status = false;
                break;
            }
        }

        return status;
    }

    private void initIntent(Context context, Class destinationActivity) {
        Intent intent = new Intent(context, destinationActivity);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == mAddNoteButton) {
            String noteTitle = mEditTextNoteTitle.getText().toString().trim();
            String noteDescription = mEditTextNoteDescription.getText().toString().trim();
            String noteContent = mEditTextNoteContent.getText().toString().trim();
            String noteTags = mEditTextNoteTags.getText().toString().trim();
            String[] fields = {noteTitle, noteDescription, noteContent, noteTags};

            Date date = new Date();

            // create a new note or update existing note object ...
            if (!validateFormFields(fields)) {
                Toast.makeText(this, "Please fill in all empty field(s).", Toast.LENGTH_SHORT).show();
                return;
            }

            final NoteEntry noteEntry = new NoteEntry(noteTitle, noteContent, noteDescription, noteTags, author, date, date);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mNoteId == DEFAULT_NOTE_ID) {
                        mDb.noteDao().insertNote(noteEntry);
                    } else {
                        noteEntry.setId(mNoteId);
                        mDb.noteDao().updatenote(noteEntry);
                    }
                    initIntent(getApplicationContext(), NotesActivity.class);
                }
            });

        }
    }
}
