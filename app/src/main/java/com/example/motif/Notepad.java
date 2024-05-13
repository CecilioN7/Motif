package com.example.motif;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Notepad extends AppCompatActivity {
    private TextInputEditText noteInput;
    private ExecutorService executor;
    private Handler handler;
    String username;

  //  private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        username = getIntent().getStringExtra("user");

        // Find the views
        noteInput = findViewById(R.id.noteInput);
        Button saveButton = findViewById(R.id.saveButton);

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        // Set up the "Save" button
        saveButton.setOnClickListener(v -> {
            // Get the note content from the input field
            String noteContent = noteInput.getText().toString();

            // Save the note content here (replace with actual saving logic)
            // e.g., Add it to a list, database, or shared preferences
            // Placeholder for saving:
            attemptNewSheet(username, noteContent);

        });
    }private void attemptNewSheet(String username, String note) {
        Log.d("Note:", note);
        if (!username.isEmpty() && !note.isEmpty()) {
            executeNewSheet(username, note);
        }
    }
    private void executeNewSheet(String username, String note){
        executor.execute(() -> {
            Boolean result = newSheet(username, note);
            Log.d("New Sheet?", String.valueOf(result));
            handler.post(() -> {
                if (result){
                    Intent toDash = new Intent(this, NotepadList.class);
                    toDash.putExtra("user", username);
                    startActivity(toDash);
                    //finish();
                } else {
                    CharSequence error = "Error adding notesheet!";
                    int dur = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, error, dur);
                    toast.show();
                }
            });
        });
    }
    private Boolean newSheet(String username, String note) {
        String apiUrl = "http://143.198.246.13/motif/api/index.php/notesheets/add?user=";
        apiUrl += username;
        apiUrl += "&notes=";
        apiUrl += note;

        String result = "";
        HttpURLConnection urlConnection;

        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");


            int responseCode = urlConnection.getResponseCode();
            StringBuffer responseOutput = new StringBuffer();

            Log.d("Response", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                    for (String line; (line = reader.readLine()) != null;){
                        responseOutput.append(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try (InputStream errorStream = urlConnection.getErrorStream();
                     BufferedReader error = new BufferedReader(new InputStreamReader(errorStream))) {
                    String line;
                    while ((line = error.readLine()) != null) {
                        responseOutput.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            result = responseOutput.toString();
            if (result.equals("[]")) {
                return false;
            } else if (result.equals("{\"Result\":true}")){
                return true;
            }

        } catch(IOException e) { //| JSONException
            e.printStackTrace();
        }

        return false;
    }
}
