package com.example.motif;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Notepad extends AppCompatActivity {
    private EditText noteInput;
  //  private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        // Find the views
        noteInput = findViewById(R.id.noteInput);
        Button saveButton = findViewById(R.id.saveButton);

        // Set up the "Save" button
        saveButton.setOnClickListener(v -> {
            // Get the note content from the input field
            String noteContent = noteInput.getText().toString().trim();

            // Save the note content here (replace with actual saving logic)
            // e.g., Add it to a list, database, or shared preferences
            // Placeholder for saving:
            saveNoteContent(noteContent);

            // Navigate back to the NotepadList activity
            Intent intent = new Intent(Notepad.this, NotepadList.class);
            startActivity(intent);

            // Finish the current activity to prevent returning here accidentally
            finish();
        });
    }

    private void saveNoteContent(String noteContent) {
        Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
       // String apiUrl = "http://143.198.246.13/motif/api/index.php/user/search?user=";
        // This is where you would save the note content.
        // placeholder for the actual saving logic.
        // For example, you could save it to a database or shared preferences.
    }
}
