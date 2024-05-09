package com.example.motif;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class Chords extends AppCompatActivity {

    private static final int MAX_COLUMNS = 3; // Adjust this value as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);

        // Retrieve the selected note passed from the previous activity
        String note = getIntent().getStringExtra("note");

        // Get reference to the main vertical layout
        LinearLayout verticalLayout = findViewById(R.id.chordList);

        // Retrieve the mapping of chord names and components for the given note
        Map<String, String> chords = getChordsForNote(note);

        // Initialize a new row layout
        LinearLayout currentRow = createNewRow();
        int columnCount = 0;

        // Loop through each chord entry
        for (Map.Entry<String, String> entry : chords.entrySet()) {
            String chordName = entry.getKey();
            String chordComponents = entry.getValue();

            // Create and configure the button
            Button chordButton = createChordButton(chordName, chordComponents);
            currentRow.addView(chordButton);
            columnCount++;

            // If the row is full, add it to the vertical layout and create a new row
            if (columnCount == MAX_COLUMNS) {
                verticalLayout.addView(currentRow);
                currentRow = createNewRow();
                columnCount = 0;
            }
        }

        // Add any remaining buttons in the last row
        if (columnCount > 0) {
            verticalLayout.addView(currentRow);
        }
    }

    /**
     * Creates a new horizontal LinearLayout to act as a row for buttons.
     */
    private LinearLayout createNewRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        row.setPadding(0, 8, 0, 8);
        return row;
    }

    /**
     * Creates a button representing a chord, with a click listener to show chord details.
     */
    private Button createChordButton(String chordName, String chordComponents) {
        Button chordButton = new Button(this);
        chordButton.setText(chordName);
        chordButton.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        ));
        chordButton.setPadding(8, 8, 8, 8);

        // Set click listener to show chord details
        chordButton.setOnClickListener(v -> showChordDetail(chordName, chordComponents));
        return chordButton;
    }

    /**
     * Retrieves chord names and their corresponding components based on the given note.
     */
    private Map<String, String> getChordsForNote(String note) {
        // Example mapping; expand to include additional notes and chords
        Map<String, String> chords = new HashMap<>();
        switch (note) {
            case "C":
                chords.put("C", "C E G");
                chords.put("Cm", "C Eb G");
                chords.put("C7", "C E G Bb");
                chords.put("Cmaj7", "C E G B");
                chords.put("Cdim", "C Eb Gb");
                chords.put("Caug", "C E G#");
                break;
            case "C#":
                chords.put("C#", "C# E# G#");
                chords.put("C#m", "C# E G#");
                chords.put("C#7", "C# E# G# B");
                chords.put("C#maj7", "C# E# G# B#");
                chords.put("C#dim", "C# E G");
                chords.put("C#aug", "C# E# G##");
                break;
            case "D":
                chords.put("D", "D F# A");
                chords.put("Dm", "D F A");
                chords.put("D7", "D F# A C");
                chords.put("Dmaj7", "D F# A C#");
                chords.put("Ddim", "D F Ab");
                chords.put("Daug", "D F# A#");
                break;
            case "D#":
                chords.put("D#", "D# G A#");
                chords.put("D#m", "D# F# A#");
                chords.put("D#7", "D# G A# C#");
                chords.put("D#maj7", "D# G A# D");
                chords.put("D#dim", "D# F# A");
                chords.put("D#aug", "D# G B");
                break;
            case "E":
                chords.put("E", "E G# B");
                chords.put("Em", "E G B");
                chords.put("E7", "E G# B D");
                chords.put("Emaj7", "E G# B D#");
                chords.put("Edim", "E G Bb");
                chords.put("Eaug", "E G# C");
                break;
            case "F":
                chords.put("F", "F A C");
                chords.put("Fm", "F Ab C");
                chords.put("F7", "F A C Eb");
                chords.put("Fmaj7", "F A C E");
                chords.put("Fdim", "F Ab B");
                chords.put("Faug", "F A C#");
                break;
            case "F#":
                chords.put("F#", "F# A# C#");
                chords.put("F#m", "F# A C#");
                chords.put("F#7", "F# A# C# E");
                chords.put("F#maj7", "F# A# C# F");
                chords.put("F#dim", "F# A C");
                chords.put("F#aug", "F# A# D");
                break;
            case "G":
                chords.put("G", "G B D");
                chords.put("Gm", "G Bb D");
                chords.put("G7", "G B D F");
                chords.put("Gmaj7", "G B D F#");
                chords.put("Gdim", "G Bb Db");
                chords.put("Gaug", "G B D#");
                break;
            case "G#":
                chords.put("G#", "G# C D#");
                chords.put("G#m", "G# B D#");
                chords.put("G#7", "G# C D# F#");
                chords.put("G#maj7", "G# C D# G");
                chords.put("G#dim", "G# B D");
                chords.put("G#aug", "G# C E");
                break;
            case "A":
                chords.put("A", "A C# E");
                chords.put("Am", "A C E");
                chords.put("A7", "A C# E G");
                chords.put("Amaj7", "A C# E G#");
                chords.put("Adim", "A C Eb");
                chords.put("Aaug", "A C# F");
                break;
            case "A#":
                chords.put("A#", "A# D F");
                chords.put("A#m", "A# C# F");
                chords.put("A#7", "A# D F G#");
                chords.put("A#maj7", "A# D F A");
                chords.put("A#dim", "A# C# E");
                chords.put("A#aug", "A# D Gb");
                break;
            case "B":
                chords.put("B", "B D# F#");
                chords.put("Bm", "B D F#");
                chords.put("B7", "B D# F# A");
                chords.put("Bmaj7", "B D# F# A#");
                chords.put("Bdim", "B D F");
                chords.put("Baug", "B D# G");
                break;
        }
        return chords;
    }

    private void showChordDetail(String chordName, String chordComponents) {
        Intent intent = new Intent(this, ChordDetail.class);
        intent.putExtra("chordName", chordName);
        intent.putExtra("chordComponents", chordComponents);
        startActivity(intent);
    }
}
