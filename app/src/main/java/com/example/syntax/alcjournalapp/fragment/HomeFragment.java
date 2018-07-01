package com.example.syntax.alcjournalapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.syntax.alcjournalapp.AddNoteActivity;
import com.example.syntax.alcjournalapp.NotesActivity;
import com.example.syntax.alcjournalapp.R;

public class HomeFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button addNewNote = (Button) view.findViewById(R.id.fragmentAddNote);
        Button viewNewNotes = (Button) view.findViewById(R.id.fragmentViewNotes);

        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initIntent(getActivity(), AddNoteActivity.class);
            }
        });

        viewNewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initIntent(getActivity(), NotesActivity.class);
            }
        });


        return view;
    }

    private void initIntent(Context context, Class destinationActivity) {
        Intent intent = new Intent(context, destinationActivity);
        startActivity(intent);
    }
}
