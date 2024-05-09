package com.example.motif;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ChordList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_list);

        findViewById(R.id.note_C).setOnClickListener(v -> openChords("C"));
        findViewById(R.id.note_Csharp).setOnClickListener(v -> openChords("C#"));
        findViewById(R.id.note_D).setOnClickListener(v -> openChords("D"));
        findViewById(R.id.note_Dsharp).setOnClickListener(v -> openChords("D#"));
        findViewById(R.id.note_E).setOnClickListener(v -> openChords("E"));
        findViewById(R.id.note_F).setOnClickListener(v -> openChords("F"));
        findViewById(R.id.note_Fsharp).setOnClickListener(v -> openChords("F#"));
        findViewById(R.id.note_G).setOnClickListener(v -> openChords("G"));
        findViewById(R.id.note_Gsharp).setOnClickListener(v -> openChords("G#"));
        findViewById(R.id.note_A).setOnClickListener(v -> openChords("A"));
        findViewById(R.id.note_Asharp).setOnClickListener(v -> openChords("A#"));
        findViewById(R.id.note_B).setOnClickListener(v -> openChords("B"));

        // Add listeners for other notes similarly
    }

    private void openChords(String note) {
        Intent intent = new Intent(this, Chords.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }
}
