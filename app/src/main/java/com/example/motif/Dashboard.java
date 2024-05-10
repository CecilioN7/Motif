package com.example.motif;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import android.os.Build;
import android.view.Window;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;


public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    // Transposition UI elements
    private EditText noteInput, transposeInput;
    private TextView resultView;
    private RadioGroup accidentalGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        Window window = getWindow();
        View decorView = window.getDecorView();

        // Retrieve the current night mode status
        int nightMode = AppCompatDelegate.getDefaultNightMode();

        // Determine the appropriate status bar color based on the night mode status
        int statusBarColor;
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            statusBarColor = R.color.desert_storm;
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            statusBarColor = R.color.cape_cod;
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        // Change the status bar color if API level is 21 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, statusBarColor));
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        materialToolbar = findViewById(R.id.materialToolbar);
//        frameLayout = findViewById(R.id.frameLayout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                Dashboard.this, drawerLayout, materialToolbar, R.string.drawer_close, R.string.drawer_open);
        drawerLayout.addDrawerListener(toggle);

        materialToolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId()==R.id.share){
                Toast.makeText(Dashboard.this, "share", Toast.LENGTH_SHORT).show();

            }
            return false;
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId()==R.id.home){
                Toast.makeText(Dashboard.this, "Home", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (menuItem.getItemId()==R.id.profile) {
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
                finish();
                //Toast.makeText(Dashboard.this, "Logout", Toast.LENGTH_SHORT).show();
                //drawerLayout.closeDrawer(GravityCompat.START);
            }else if (menuItem.getItemId()==R.id.transpose) {
                Intent intent = new Intent(Dashboard.this, TunerActivity.class);
                startActivity(intent);
                //Toast.makeText(Dashboard.this, "Transpose", Toast.LENGTH_SHORT).show();
                //drawerLayout.closeDrawer(GravityCompat.START);
            }else if (menuItem.getItemId()==R.id.settings) {
                Intent intent = new Intent(Dashboard.this, Settings.class);
                intent.putExtra("user", getIntent().getStringExtra("user"));

                startActivity(intent);
                //Toast.makeText(Dashboard.this, "Settings", Toast.LENGTH_SHORT).show();
                //drawerLayout.closeDrawer(GravityCompat.START);
            }else if (menuItem.getItemId()==R.id.notepad) {
                Intent intent = new Intent(Dashboard.this, NotepadList.class);
                intent.putExtra("user", getIntent().getStringExtra("user"));

                startActivity(intent);
                //Toast.makeText(Dashboard.this, "Settings", Toast.LENGTH_SHORT).show();
                //drawerLayout.closeDrawer(GravityCompat.START);
            }else if (menuItem.getItemId()==R.id.chord) {
                Intent intent = new Intent(Dashboard.this, ChordList.class);
                intent.putExtra("user", getIntent().getStringExtra("user"));

                startActivity(intent);
                //Toast.makeText(Dashboard.this, "Settings", Toast.LENGTH_SHORT).show();
                //drawerLayout.closeDrawer(GravityCompat.START);
            }
            return false;
        });
        // Find views
        drawerLayout = findViewById(R.id.drawerLayout);
        materialToolbar = findViewById(R.id.materialToolbar);
        navigationView = findViewById(R.id.navigationView);
        noteInput = findViewById(R.id.noteInput);
        transposeInput = findViewById(R.id.transposeInput);
        resultView = findViewById(R.id.resultView);
        accidentalGroup = findViewById(R.id.accidentalGroup);
        Button transposeButton = findViewById(R.id.transposeButton);
        Button copyButton = findViewById(R.id.copyButton);
        // Transpose button click handler
        transposeButton.setOnClickListener(v -> transposeNotes());

        // Copy to Clipboard button click handler
        copyButton.setOnClickListener(v -> copyToClipboard());
    }
    // Transpose notes logic
    private void transposeNotes() {
        String notes = noteInput.getText().toString().trim();
        int transposeValue;
        try {
            transposeValue = Integer.parseInt(transposeInput.getText().toString().trim());
        } catch (NumberFormatException e) {
            resultView.setText("Invalid transpose value.");
            return;
        }

        if (transposeValue < -12 || transposeValue > 12) {
            resultView.setText("Enter a number between -12 and 12.");
            return;
        }

        int accidental = (accidentalGroup.getCheckedRadioButtonId() == R.id.sharpButton) ? 1 : 2;

        StringBuilder transposedNotes = new StringBuilder();
        String[] noteLines = notes.split("\n");

        for (String line : noteLines) {
            StringBuilder transposedLine = new StringBuilder();
            String[] noteArray = line.split("(?<=\\s)|(?=\\s)");

            for (String note : noteArray) {
                String trimmedNote = note.trim();
                boolean isMinor = trimmedNote.endsWith("m");

                if (isMinor) {
                    trimmedNote = trimmedNote.substring(0, trimmedNote.length() - 1);
                }

                if (trimmedNote.isEmpty()) {
                    // If it's a space or empty string, retain it
                    transposedLine.append(note);
                    continue;
                }

                int position = getNotePosition(trimmedNote);

                if (position == -1) {
                    // If the note is invalid, retain the original string
                    transposedLine.append(trimmedNote);
                    if (isMinor) transposedLine.append("m");
                    continue;
                }

                int newPosition;
                if (transposeValue >= 0) {
                    newPosition = transposeUp(position, transposeValue);
                } else {
                    newPosition = transposeDown(position, transposeValue);
                }

                String transposedNote = displayNewNotePosition(newPosition, accidental);
                transposedLine.append(transposedNote);
                if (isMinor) transposedLine.append("m");
            }

            transposedNotes.append(transposedLine.toString()).append("\n");
        }

        resultView.setText(transposedNotes.toString().trim());
    }

    // Copy output to clipboard
    private void copyToClipboard() {
        String resultText = resultView.getText().toString();
        if (resultText.isEmpty()) {
            Toast.makeText(this, "No output to copy!", Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Transposed Notes", resultText);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    // Helper methods for transposition logic
    private int getNotePosition(String note) {
        switch (note) {
            case "A": return 1;
            case "A#": case "Bb": return 2;
            case "B": return 3;
            case "C": return 4;
            case "C#": case "Db": return 5;
            case "D": return 6;
            case "D#": case "Eb": return 7;
            case "E": return 8;
            case "F": return 9;
            case "F#": case "Gb": return 10;
            case "G": return 11;
            case "G#": case "Ab": return 12;
            default: return -1;
        }
    }

    private int transposeUp(int position, int distance) {
        return (position <= 12 - distance) ? position + distance : position + distance - 12;
    }

    private int transposeDown(int position, int distance) {
        return (position > Math.abs(distance)) ? position + distance : position + distance + 12;
    }

    private String displayNewNotePosition(int position, int accidental) {
        switch (position) {
            case 1: return "A";
            case 2: return (accidental == 1) ? "A#" : "Bb";
            case 3: return "B";
            case 4: return "C";
            case 5: return (accidental == 1) ? "C#" : "Db";
            case 6: return "D";
            case 7: return (accidental == 1) ? "D#" : "Eb";
            case 8: return "E";
            case 9: return "F";
            case 10: return (accidental == 1) ? "F#" : "Gb";
            case 11: return "G";
            case 12: return (accidental == 1) ? "G#" : "Ab";
            default: return "";
        }
    }
}


