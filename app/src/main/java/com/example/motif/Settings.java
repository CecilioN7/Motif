package com.example.motif;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Settings extends AppCompatActivity {
    private TextInputEditText currentPasswordEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputEditText confirmPasswordEditText;
    private ExecutorService executor;
    private ExecutorService executor2;
    private Handler handler;
    private Handler handler2;
    String username = "";
    String dbPass = "";

    Switch switcher;
    boolean nightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        currentPasswordEditText = findViewById(R.id.currentPasswordInput);
        newPasswordEditText = findViewById(R.id.newPasswordInput);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordInput);
        executor = Executors.newSingleThreadExecutor();
        executor2 = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        handler2 = new Handler(Looper.getMainLooper());
        Button changePassword = findViewById(R.id.changePassword);

        changePassword.setOnClickListener(v -> attemptPasswordChange());

        switcher = findViewById(R.id.switcher);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false);

        if(nightMODE) {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(view -> {

            if (nightMODE){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("night", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("night", true);
            }
            editor.apply();
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void attemptPasswordChange() {
        String currentPassword = Objects.requireNonNull(currentPasswordEditText.getText()).toString();
        String newPassword = Objects.requireNonNull(newPasswordEditText.getText()).toString();
        String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString();

        Log.d("currPass:", currentPassword);
        Log.d("newPass:", newPassword);
        Log.d("confPass:", confirmPassword);

        Intent intent = getIntent();
        username = intent.getStringExtra("user");

        String newPassHash;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(newPassword.getBytes(StandardCharsets.UTF_8));
            newPassHash = bytesToHex(encodedHash);
            Log.d("newPassHash:", newPassHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Log.d("Username", username);
        if (!username.isEmpty() && !currentPassword.isEmpty() && !confirmPassword.isEmpty() && !newPassword.isEmpty() && newPassword.equals(confirmPassword)){
            executeCheckPass(username, currentPassword, newPassHash);
        }

    }
    private void executeCheckPass(String username, String password, String newPassHash){
        executor.execute(() -> {
            Boolean result = checkPassword(username, password);
            handler.post(() -> {
                if (result){
                    Log.d("Executing...", newPassHash);
                    executePasswordChange(newPassHash);
                }
            });
        });
    }
    private Boolean checkPassword(String username, String password){
        HttpURLConnection urlConnection;
        String result;
        String hash = "";
        JSONArray returnedJSON;
        String returnedPass;
        JSONObject jsonResponse;

        String apiUrl = "http://143.198.246.13/motif/api/index.php/user/search?user=";

        apiUrl = apiUrl + username;
        Log.d("API:", apiUrl);
        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            StringBuffer responseOutput = new StringBuffer();
            Scanner scanner = new Scanner(url.openStream());

            Log.d("Response", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                hash = bytesToHex(encodedHash);
                scanner.close();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                    for (String line; (line = reader.readLine()) != null; ) {
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
            }
            returnedJSON = new JSONArray(result);


            returnedPass = returnedJSON.getString(0);
            jsonResponse = new JSONObject(returnedPass);
            returnedPass = jsonResponse.getString("password");
            dbPass = returnedPass;
            Log.d("Hashed current pass", hash);
            Log.d("Database pass:", dbPass);

            return dbPass.equals(hash);

        } catch (IOException e) { //| JSONException
            e.printStackTrace();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private void executePasswordChange(String newPassword) {
        executor2.execute(() -> {
            Log.d("New Pass:", newPassword);
            Boolean result = changePassword(newPassword);
            Log.d("New Password?", String.valueOf(result));
            handler2.post(() -> {
                if (result) {
                    Toast.makeText(this, "Password changed!", Toast.LENGTH_SHORT).show();
                    Intent toSettings = new Intent(this, Settings.class);
                    startActivity(toSettings);
                } else {
                    CharSequence error = "Current password incorrect!";
                    int dur = Toast.LENGTH_SHORT;
                    Toast.makeText(this, error, dur).show();
                }
            });
        });
    }

    private Boolean changePassword(String newPassword) {
        String apiUrl = "http://143.198.246.13/motif/api/index.php/user/changePassword?username=";
        apiUrl = apiUrl + username;
        apiUrl = apiUrl + "&password=";
        Log.d("New password:", newPassword);
        apiUrl = apiUrl + newPassword;

        Log.d("API:", apiUrl);
        String result;
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            StringBuilder responseOutput = new StringBuilder();

            Log.d("Response", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        responseOutput.append(line);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
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
            Log.d("Result::", result);

            return result.equals("{\"Result\":true}");

        } catch (IOException e) { //| JSONException
            e.printStackTrace();
        }
        return false;
    }
}