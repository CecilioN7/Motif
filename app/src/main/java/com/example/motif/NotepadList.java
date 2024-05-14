package com.example.motif;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
//import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotepadList extends AppCompatActivity {
    private List<String> notes = new ArrayList<>();
    private NotesAdapter adapter;
    private ExecutorService executor;
    private Handler handler;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_list);

        // Initialize RecyclerView and Adapter
        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(notes);
        notesRecyclerView.setAdapter(adapter);

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        Intent userintent = getIntent();
        username = userintent.getStringExtra("user");

        // Apply swipe-to-delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(notesRecyclerView);


        attemptNotesheets();
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        adapter.notifyDataSetChanged();
        // New Note Button
        Button newNoteButton = findViewById(R.id.newNoteButton);
        newNoteButton.setOnClickListener(v -> {
            // Navigate to the Notepad activity for creating a new note
            Intent intent = new Intent(NotepadList.this, Notepad.class);
            intent.putExtra("user", getIntent().getStringExtra("user"));
            startActivity(intent);
        });
    }
    private void attemptNotesheets(){
        executeNotesheets(username);
    }
    private void executeNotesheets(String username){
        executor.execute(() -> {
            Boolean result = getNotesheets(username);
            handler.post(() -> {
                if (result){
                    CharSequence error = "Notesheets loaded!";
                    int dur = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, error, dur);
                    toast.show();


                }
            });
        });
    }
    private String getSheetByID(String ID){
        String apiUrl = "http://143.198.246.13/motif/api/index.php/notesheets/sheet?ID=";
        String result;
        apiUrl += ID;
        HttpURLConnection urlConnection;
        JSONArray returnedJSON;
        JSONObject jsonResponse;
        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            int responseCode = urlConnection.getResponseCode();
            StringBuilder responseOutput = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            Log.d("Response", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                scanner.close();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
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
                return String.valueOf(false);
            }
            returnedJSON = new JSONArray(result);
            String returnedSheet = returnedJSON.getString(0);
            jsonResponse = new JSONObject(returnedSheet);
            returnedSheet = jsonResponse.getString("notes");
            return returnedSheet;
        } catch(IOException e){ //| JSONException
            e.printStackTrace();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(false);
    }
    private Boolean getNotesheets(String username) {
        String apiUrl = "http://143.198.246.13/motif/api/index.php/notesheets/list?user=";
        String result;
        apiUrl += username;
        HttpURLConnection urlConnection;
        JSONArray returnedJSON;
        JSONObject jsonResponse;
        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            int responseCode = urlConnection.getResponseCode();
            StringBuilder responseOutput = new StringBuilder();

            Log.d("Response", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
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
                synchronized (this){
                    notifyAll();
                }
                return false;
            }
            returnedJSON = new JSONArray(result);

            try{
                for (int i =0; i <returnedJSON.length(); i++){
                    String returnedSheet = returnedJSON.getString(i);
                    jsonResponse = new JSONObject(returnedSheet);
                    returnedSheet = jsonResponse.getString("sheetID");

                    String note = getSheetByID(returnedSheet);
                    notes.add(note);

                }
                synchronized (this){
                    notifyAll();
                }
                return true;
            } catch(JSONException e){
                e.printStackTrace();
            }
        } catch(IOException e){ //| JSONException
            e.printStackTrace();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    // Override the back button behavior to return to the Dashboard activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotepadList.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
