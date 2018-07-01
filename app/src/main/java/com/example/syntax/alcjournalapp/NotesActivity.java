package com.example.syntax.alcjournalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import android.support.design.widget.FloatingActionButton;

import com.example.syntax.alcjournalapp.database.AppDatabase;
import com.example.syntax.alcjournalapp.database.NoteEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class NotesActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener, View.OnClickListener{

    public static final String TAG = NotesActivity.class.getSimpleName();

    private NoteAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Toast mToast;
    private  FloatingActionButton mFab;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "You're not login, please do.", Toast.LENGTH_SHORT).show();
            initIntent(this, LoginActivity.class);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NoteAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        /**
         * An ItemTouchHelper enables touch behavior (like swipe and move).
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<NoteEntry> notes = mAdapter.getNotes();
                        mDb.noteDao().deleteNote(notes.get((position)));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);


        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mDb = AppDatabase.getsInstance(getApplicationContext());

        setupNotesViewModel();
    }

    private void setupNotesViewModel() {
        NotesViewModel noteViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        noteViewModel.getNotes().observe(this, new Observer<List<NoteEntry>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntry> noteEntries) {
                mAdapter.setNotes(noteEntries);
            }
        });
    }

    private void initIntent(Context context, Class destinationActivity) {
        Intent intent = new Intent(context, destinationActivity);
        startActivity(intent);
    }

    private void initIntentWithExtra(Context context, Class destinationActivity, int itemId) {
        Intent intent = new Intent(context, destinationActivity);
        intent.putExtra(AddNoteActivity.EXTRA_NOTE_ID, itemId);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        initIntentWithExtra(this, AddNoteActivity.class, clickedItemIndex);
    }


    @Override
    public void onClick(View view) {
        if (view == mFab) {
            initIntent(this, AddNoteActivity.class);
        }
    }

}
