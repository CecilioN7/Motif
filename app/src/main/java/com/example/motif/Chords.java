package com.example.motif;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class Chords extends AppCompatActivity {

    private static final int MAX_COLUMNS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);

        // Retrieve the selected note passed from the previous activity
        String note = getIntent().getStringExtra("note");

        // Get reference to the main vertical layout
        LinearLayout verticalLayout = findViewById(R.id.chordList);

        // Retrieve the mapping of chord names and components for the given note
        assert note != null;
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

    private Button createChordButton(String chordName, String chordComponents) {
        Button chordButton = new Button(this, null, 0, R.style.BUTTON);
        chordButton.setText(chordName);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        params.setMargins(0, 0, 20, 10); // Right and bottom margins, adjust as needed

        chordButton.setLayoutParams(params);

        // Increase padding for a larger button appearance
        int paddingInDp = (int) (12 * getResources().getDisplayMetrics().density); // Convert 12dp to pixels
        chordButton.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);

        // Set gravity to center the text within the button
        chordButton.setGravity(android.view.Gravity.CENTER);

        // Set click listener to show chord details
        chordButton.setOnClickListener(v -> showChordDetail(chordName, chordComponents));
        return chordButton;
    }

    private Map<String, String> getChordsForNote(String note) {
        // Example mapping; expand to include additional notes and chords
        Map<String, String> chords = new HashMap<>();
        switch (note) {
            case "C":
                chords.put("C", "C E G");
                chords.put("Cm", "C Eb G");
                chords.put("C7", "C E G Bb");
                chords.put("Cm7", "C Eb G Bb");
                chords.put("Cmaj7", "C E G B");
                chords.put("CmM7", "C Eb G B");
                chords.put("C6", "C E G A");
                chords.put("Cm6", "C Eb G A");
                chords.put("C9", "C E G Bb D");
                chords.put("Cm9", "C Eb G Bb D");
                chords.put("C11", "C E G Bb D F");
                chords.put("Cm11", "C Eb G Bb D F");
                chords.put("Cmaj9", "C E G B D");
                chords.put("Cmaj11", "C E G B D F");
                chords.put("C13", "C E G Bb D F A");
                chords.put("Cm13", "C Eb G Bb D F A");
                chords.put("Cmaj13", "C E G B D F A");
                chords.put("Cadd9", "C E G D");
                chords.put("C7-5", "C E Gb Bb");
                chords.put("C7+5", "C E G# Bb");
                chords.put("Csus", "C F G");
                chords.put("Cdim", "C Eb Gb");
                chords.put("Cdim7", "C Eb Gb A");
                chords.put("Cm7b5", "C Eb Gb Bb");
                chords.put("Caug", "C E G#");
                chords.put("Caug7", "C E G# Bb");
                break;

            case "C#":
                chords.put("C#", "C# E# G#");
                chords.put("C#m", "C# E G#");
                chords.put("C#7", "C# E# G# B");
                chords.put("C#m7", "C# E G# B");
                chords.put("C#maj7", "C# E# G# B#");
                chords.put("C#mM7", "C# E G# B#");
                chords.put("C#6", "C# E# G# A#");
                chords.put("C#m6", "C# E G# A#");
                chords.put("C#9", "C# E# G# B D#");
                chords.put("C#m9", "C# E G# B D#");
                chords.put("C#11", "C# E# G# B D# F#");
                chords.put("C#m11", "C# E G# B D# F#");
                chords.put("C#maj9", "C# E# G# B# D#");
                chords.put("C#maj11", "C# E# G# B# D# F#");
                chords.put("C#13", "C# E# G# B D# F# A#");
                chords.put("C#m13", "C# E G# B D# F# A#");
                chords.put("C#maj13", "C# E# G# B# D# F# A#");
                chords.put("C#add9", "C# E# G# D#");
                chords.put("C#7-5", "C# E# G B");
                chords.put("C#7+5", "C# E# A B");
                chords.put("C#sus", "C# F# G#");
                chords.put("C#dim", "C# E G");
                chords.put("C#dim7", "C# E G Bb");
                chords.put("C#m7b5", "C# E G B");
                chords.put("C#aug", "C# E# G##");
                chords.put("C#aug7", "C# E# G## B");
                break;


            case "D":
                chords.put("D", "D F# A");
                chords.put("Dm", "D F A");
                chords.put("D7", "D F# A C");
                chords.put("Dm7", "D F A C");
                chords.put("Dmaj7", "D F# A C#");
                chords.put("DmM7", "D F A C#");
                chords.put("D6", "D F# A B");
                chords.put("Dm6", "D F A B");
                chords.put("D9", "D F# A C E");
                chords.put("Dm9", "D F A C E");
                chords.put("D11", "D F# A C E G");
                chords.put("Dm11", "D F A C E G");
                chords.put("Dmaj9", "D F# A C# E");
                chords.put("Dmaj11", "D F# A C# E G");
                chords.put("D13", "D F# A C E G B");
                chords.put("Dm13", "D F A C E G B");
                chords.put("Dmaj13", "D F# A C# E G B");
                chords.put("Dadd9", "D F# A E");
                chords.put("D7-5", "D F# Ab C");
                chords.put("D7+5", "D F# A# C");
                chords.put("Dsus", "D G A");
                chords.put("Ddim", "D F Ab");
                chords.put("Ddim7", "D F Ab B");
                chords.put("Dm7b5", "D F Ab C");
                chords.put("Daug", "D F# A#");
                chords.put("Daug7", "D F# A# C");
                break;

            case "D#":
                chords.put("D#", "D# G A#");
                chords.put("D#m", "D# F# A#");
                chords.put("D#7", "D# G A# C#");
                chords.put("D#m7", "D# F# A# C#");
                chords.put("D#maj7", "D# G A# D");
                chords.put("D#mM7", "D# F# A# D");
                chords.put("D#6", "D# G A# C");
                chords.put("D#m6", "D# F# A# C");
                chords.put("D#9", "D# G A# C# F");
                chords.put("D#m9", "D# F# A# C# F");
                chords.put("D#11", "D# G A# C# F G#");
                chords.put("D#m11", "D# F# A# C# F G#");
                chords.put("D#maj9", "D# G A# C# F");
                chords.put("D#maj11", "D# G A# C# F G#");
                chords.put("D#13", "D# G A# C# F G# B");
                chords.put("D#m13", "D# F# A# C# F G# B");
                chords.put("D#maj13", "D# G A# C# F G# B");
                chords.put("D#add9", "D# G A# F");
                chords.put("D#7-5", "D# G B C#");
                chords.put("D#7+5", "D# G C C#");
                chords.put("D#sus", "D# G# A#");
                chords.put("D#dim", "D# F# A");
                chords.put("D#dim7", "D# F# A C");
                chords.put("D#m7b5", "D# F# A C#");
                chords.put("D#aug", "D# G B");
                chords.put("D#aug7", "D# G B C#");
                break;


            case "E":
                chords.put("E", "E G# B");
                chords.put("Em", "E G B");
                chords.put("E7", "E G# B D");
                chords.put("Em7", "E G B D");
                chords.put("Emaj7", "E G# B D#");
                chords.put("EmM7", "E G B D#");
                chords.put("E6", "E G# B C#");
                chords.put("Em6", "E G B C#");
                chords.put("E9", "E G# B D F#");
                chords.put("Em9", "E G B D F#");
                chords.put("E11", "E G# B D F# A");
                chords.put("Em11", "E G B D F# A");
                chords.put("Emaj9", "E G# B D# F#");
                chords.put("Emaj11", "E G# B D# F# A");
                chords.put("E13", "E G# B D F# A C#");
                chords.put("Em13", "E G B D F# A C#");
                chords.put("Emaj13", "E G# B D# F# A C#");
                chords.put("Eadd9", "E G# B F#");
                chords.put("E7-5", "E G# Bb D");
                chords.put("E7+5", "E G# C D");
                chords.put("Esus", "E A B");
                chords.put("Edim", "E G Bb");
                chords.put("Edim7", "E G Bb C#");
                chords.put("Em7b5", "E G Bb D");
                chords.put("Eaug", "E G# C");
                chords.put("Eaug7", "E G# C D");
                break;

            case "F":
                chords.put("F", "F A C");
                chords.put("Fm", "F Ab C");
                chords.put("F7", "F A C Eb");
                chords.put("Fm7", "F Ab C Eb");
                chords.put("Fmaj7", "F A C E");
                chords.put("FmM7", "F Ab C E");
                chords.put("F6", "F A C D");
                chords.put("Fm6", "F Ab C D");
                chords.put("F9", "F A C Eb G");
                chords.put("Fm9", "F Ab C Eb G");
                chords.put("F11", "F A C Eb G Bb");
                chords.put("Fm11", "F Ab C Eb G Bb");
                chords.put("Fmaj9", "F A C E G");
                chords.put("Fmaj11", "F A C E G Bb");
                chords.put("F13", "F A C Eb G Bb D");
                chords.put("Fm13", "F Ab C Eb G Bb D");
                chords.put("Fmaj13", "F A C E G Bb D");
                chords.put("Fadd9", "F A C G");
                chords.put("F7-5", "F A B Eb");
                chords.put("F7+5", "F A D Eb");
                chords.put("Fsus", "F Bb C");
                chords.put("Fdim", "F Ab B");
                chords.put("Fdim7", "F Ab B D");
                chords.put("Fm7b5", "F Ab B Eb");
                chords.put("Faug", "F A C#");
                chords.put("Faug7", "F A C# Eb");
                break;

            case "F#":
                chords.put("F#", "F# A# C#");
                chords.put("F#m", "F# A C#");
                chords.put("F#7", "F# A# C# E");
                chords.put("F#m7", "F# A C# E");
                chords.put("F#maj7", "F# A# C# F");
                chords.put("F#mM7", "F# A C# F");
                chords.put("F#6", "F# A# C# D#");
                chords.put("F#m6", "F# A C# D#");
                chords.put("F#9", "F# A# C# E G#");
                chords.put("F#m9", "F# A C# E G#");
                chords.put("F#11", "F# A# C# E G# B");
                chords.put("F#m11", "F# A C# E G# B");
                chords.put("F#maj9", "F# A# C# F G#");
                chords.put("F#maj11", "F# A# C# F G# B");
                chords.put("F#13", "F# A# C# E G# B D#");
                chords.put("F#m13", "F# A C# E G# B D#");
                chords.put("F#maj13", "F# A# C# F G# B D#");
                chords.put("F#add9", "F# A# C# G#");
                chords.put("F#7-5", "F# A# C Eb");
                chords.put("F#7+5", "F# A# D Eb");
                chords.put("F#sus", "F# B C#");
                chords.put("F#dim", "F# A C");
                chords.put("F#dim7", "F# A C D#");
                chords.put("F#m7b5", "F# A C E");
                chords.put("F#aug", "F# A# D");
                chords.put("F#aug7", "F# A# D E");
                break;

            case "G":
                chords.put("G", "G B D");
                chords.put("Gm", "G Bb D");
                chords.put("G7", "G B D F");
                chords.put("Gm7", "G Bb D F");
                chords.put("Gmaj7", "G B D F#");
                chords.put("GmM7", "G Bb D F#");
                chords.put("G6", "G B D E");
                chords.put("Gm6", "G Bb D E");
                chords.put("G9", "G B D F A");
                chords.put("Gm9", "G Bb D F A");
                chords.put("G11", "G B D F A C");
                chords.put("Gm11", "G Bb D F A C");
                chords.put("Gmaj9", "G B D F# A");
                chords.put("Gmaj11", "G B D F# A C");
                chords.put("G13", "G B D F A C E");
                chords.put("Gm13", "G Bb D F A C E");
                chords.put("Gmaj13", "G B D F# A C E");
                chords.put("Gadd9", "G B D A");
                chords.put("G7-5", "G B C# F");
                chords.put("G7+5", "G B E F");
                chords.put("Gsus", "G C D");
                chords.put("Gdim", "G Bb Db");
                chords.put("Gdim7", "G Bb Db E");
                chords.put("Gm7b5", "G Bb Db F");
                chords.put("Gaug", "G B D#");
                chords.put("Gaug7", "G B D# F");
                break;

            case "G#":
                chords.put("G#", "G# C D#");
                chords.put("G#m", "G# B D#");
                chords.put("G#7", "G# C D# F#");
                chords.put("G#m7", "G# B D# F#");
                chords.put("G#maj7", "G# C D# G");
                chords.put("G#mM7", "G# B D# G");
                chords.put("G#6", "G# C D# F");
                chords.put("G#m6", "G# B D# F");
                chords.put("G#9", "G# C D# F# A#");
                chords.put("G#m9", "G# B D# F# A#");
                chords.put("G#11", "G# C D# F# A# C#");
                chords.put("G#m11", "G# B D# F# A# C#");
                chords.put("G#maj9", "G# C D# G A#");
                chords.put("G#maj11", "G# C D# G A# C#");
                chords.put("G#13", "G# C D# F# A# C# E");
                chords.put("G#m13", "G# B D# F# A# C# E");
                chords.put("G#maj13", "G# C D# G A# C# E");
                chords.put("G#add9", "G# C D# A#");
                chords.put("G#7-5", "G# C D F#");
                chords.put("G#7+5", "G# C E F#");
                chords.put("G#sus", "G# C# D#");
                chords.put("G#dim", "G# B D");
                chords.put("G#dim7", "G# B D F");
                chords.put("G#m7b5", "G# B D F#");
                chords.put("G#aug", "G# C E");
                chords.put("G#aug7", "G# C E F#");
                break;

            case "A":
                chords.put("A", "A C# E");
                chords.put("Am", "A C E");
                chords.put("A7", "A C# E G");
                chords.put("Am7", "A C E G");
                chords.put("Amaj7", "A C# E G#");
                chords.put("AmM7", "A C E G#");
                chords.put("A6", "A C# E F#");
                chords.put("Am6", "A C E F#");
                chords.put("A9", "A C# E G B");
                chords.put("Am9", "A C E G B");
                chords.put("A11", "A C# E G B D");
                chords.put("Am11", "A C E G B D");
                chords.put("Amaj9", "A C# E G# B");
                chords.put("Amaj11", "A C# E G# B D");
                chords.put("A13", "A C# E G B D F#");
                chords.put("Am13", "A C E G B D F#");
                chords.put("Amaj13", "A C# E G# B D F#");
                chords.put("Aadd9", "A C# E B");
                chords.put("A7-5", "A C# D# G");
                chords.put("A7+5", "A C# F G");
                chords.put("Asus", "A D E");
                chords.put("Adim", "A C Eb");
                chords.put("Adim7", "A C Eb Gb");
                chords.put("Am7b5", "A C Eb G");
                chords.put("Aaug", "A C# F");
                chords.put("Aaug7", "A C# F G");
                break;

            case "A#":
                chords.put("A#", "A# D F");
                chords.put("A#m", "A# C# F");
                chords.put("A#7", "A# D F G#");
                chords.put("A#m7", "A# C# F G#");
                chords.put("A#maj7", "A# D F A");
                chords.put("A#mM7", "A# C# F A");
                chords.put("A#6", "A# D F G");
                chords.put("A#m6", "A# C# F G");
                chords.put("A#9", "A# D F G# C");
                chords.put("A#m9", "A# C# F G# C");
                chords.put("A#11", "A# D F G# C Eb");
                chords.put("A#m11", "A# C# F G# C Eb");
                chords.put("A#maj9", "A# D F A C");
                chords.put("A#maj11", "A# D F A C Eb");
                chords.put("A#13", "A# D F G# C Eb G");
                chords.put("A#m13", "A# C# F G# C Eb G");
                chords.put("A#maj13", "A# D F A C Eb G");
                chords.put("A#add9", "A# D F C");
                chords.put("A#7-5", "A# D E G#");
                chords.put("A#7+5", "A# D Gb G#");
                chords.put("A#sus", "A# D# F");
                chords.put("A#dim", "A# C# E");
                chords.put("A#dim7", "A# C# E G");
                chords.put("A#m7b5", "A# C# E G#");
                chords.put("A#aug", "A# D Gb");
                chords.put("A#aug7", "A# D Gb G#");
                break;

            case "B":
                chords.put("B", "B D# F#");
                chords.put("Bm", "B D F#");
                chords.put("B7", "B D# F# A");
                chords.put("Bm7", "B D F# A");
                chords.put("Bmaj7", "B D# F# A#");
                chords.put("BmM7", "B D F# A#");
                chords.put("B6", "B D# F# G#");
                chords.put("Bm6", "B D F# G#");
                chords.put("B9", "B D# F# A C#");
                chords.put("Bm9", "B D F# A C#");
                chords.put("B11", "B D# F# A C# E");
                chords.put("Bm11", "B D F# A C# E");
                chords.put("Bmaj9", "B D# F# A# C#");
                chords.put("Bmaj11", "B D# F# A# C# E");
                chords.put("B13", "B D# F# A C# E G#");
                chords.put("Bm13", "B D F# A C# E G#");
                chords.put("Bmaj13", "B D# F# A# C# E G#");
                chords.put("Badd9", "B D# F# C#");
                chords.put("B7-5", "B D# E A");
                chords.put("B7+5", "B D# G A");
                chords.put("Bsus", "B E F#");
                chords.put("Bdim", "B D F");
                chords.put("Bdim7", "B D F G#");
                chords.put("Bm7b5", "B D F A");
                chords.put("Baug", "B D# G");
                chords.put("Baug7", "B D# G A");
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
