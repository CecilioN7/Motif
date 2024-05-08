package com.example.motif;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotepadList extends AppCompatActivity {
    private List<String> notes = new ArrayList<>();
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_list);

        // Initialize RecyclerView and Adapter
        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(notes);
        notesRecyclerView.setAdapter(adapter);

        // Apply swipe-to-delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(notesRecyclerView);

        // Load your notes (replace this with actual data loading)
        notes.add("Sample Note 1\nDetails of the first sample note.");
        notes.add("Sample Note 2\nDetails of the second sample note.");
        adapter.notifyDataSetChanged();

        // New Note Button
        Button newNoteButton = findViewById(R.id.newNoteButton);
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Notepad activity for creating a new note
                Intent intent = new Intent(NotepadList.this, Notepad.class);
                startActivity(intent);
            }
        });
    }

    // Override the back button behavior to return to the Dashboard activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotepadList.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
        finish();
    }
}
