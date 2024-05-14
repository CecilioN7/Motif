package com.example.motif;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChordDetail extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_detail);

        String chordName = getIntent().getStringExtra("chordName");
        String chordComponents = getIntent().getStringExtra("chordComponents");

        TextView chordTitle = findViewById(R.id.chordTitle);
        TextView chordNotes = findViewById(R.id.chordNotes);

        chordTitle.setText(chordName);
        chordNotes.setText("Notes: " + chordComponents);
    }
}
