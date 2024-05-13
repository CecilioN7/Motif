package com.example.motif;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

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
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.validator.routines.EmailValidator;
public class SignUp extends AppCompatActivity {
    TextInputEditText usernameEditText;
    TextInputEditText passwordEditText;
    TextInputEditText emailEditText;
    TextInputEditText phoneEditText;
    TextInputEditText nameEditText;
    private ExecutorService executor;
    private Handler handler;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.usernameInp);
        passwordEditText = findViewById(R.id.passwordInp);
        emailEditText = findViewById(R.id.emailInp);
        phoneEditText = findViewById(R.id.phoneInp);
        nameEditText = findViewById(R.id.nameInp);

        Button register = findViewById(R.id.register);
        Button toLogin = findViewById(R.id.toLogin);

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(v -> attemptRegister());
    }
    protected void onDestroy(){
        super.onDestroy();
        if (executor != null){
            executor.shutdown();
        }
    }

    private void attemptRegister(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String name = nameEditText.getText().toString();

        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !name.isEmpty()) {
            executeRegister(username, password, email, name, phone);
        }
    }
    private void executeRegister(String username, String password, String email, String name, String phone){
        executor.execute(() -> {
            Boolean result = register(username, password, email, name, phone);
            Log.d("Register", String.valueOf(result));
            handler.post(() -> {
                if (result){
                    Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                    Intent toLogin = new Intent(this, Login.class);
                    startActivity(toLogin);
                } else {
                    CharSequence error = "Error registering. Check your credentials!";
                    int dur = Toast.LENGTH_SHORT;
                    Toast.makeText(this, error, dur).show();
                }
            });
        });
    }
    private Boolean checkRegisteredUsername(String username){
        String apiUrl = "http://143.198.246.13/motif/api/index.php/user/search?user=";
        String result;
        HttpURLConnection urlConnection;
        apiUrl = apiUrl+ username;
        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect();
            StringBuffer responseOutput = new StringBuffer();
            Scanner scanner = new Scanner(url.openStream());
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                scanner.close();
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
            return !result.equals("[]"); //Returns false if user does not exist
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Boolean register(String username, String password, String email, String name, String phone) {
        String apiUrl = "http://143.198.246.13/motif/api/index.php/user/add?user=";

        Log.d("Username", username);
        Log.d("password", password);
        if (!EmailValidator.getInstance().isValid(email)){
            return false;
        } else if (checkRegisteredUsername(username)){
            return false;
        }
        apiUrl = apiUrl+ username;
        apiUrl = apiUrl + "&pass=" + password;
        apiUrl = apiUrl + "&email=" + email;
        apiUrl = apiUrl + "&name=" + name;
        apiUrl = apiUrl + "&phone=" + phone;
        Log.d("Url:", apiUrl);
        String result = "";
        HttpURLConnection urlConnection;

        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            StringBuffer responseOutput = new StringBuffer();

            Log.d("Response", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                    for (String line; (line = reader.readLine()) != null;){
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
            Log.d("Result before JSON:", result);
            String error = "Error: Duplicate entry '";
            error = error + username + "' for key 'user.";
            error = error + "username'";
            Log.d("Error", error);

            if (result.equals("{\"Result\":true}")){
                return true;
            } else {
                if (result.equals(error)){
                    return true;
                } else {
                    error = "Error: Duplicate entry '";
                    error = error + email + "' for key 'user.";
                    error = error + "email'";
                    Log.d("Error2", error);

                    return false;
                }
            }


        } catch(IOException e){ //| JSONException
            e.printStackTrace();

        }

        if (result.equals("true")) {
            return true;
        } else {
            return false;
        }
    }
}
