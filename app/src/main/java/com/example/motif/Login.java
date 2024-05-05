package com.example.motif;

import static android.text.TextUtils.concat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private ExecutorService executor;
    private Handler handler;
	
	Button callSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameInput);
        passwordEditText = findViewById(R.id.passwordInput);
        Button submitButton = findViewById(R.id.submit);
		callSignUp = findViewById(R.id.signup_screen);
		callSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        submitButton.setOnClickListener(v -> attemptLogin());
    }
    protected void onDestroy(){
        super.onDestroy();
        if (executor != null){
            executor.shutdown();
        }
    }

    private void attemptLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            executeLogin(username, password);
        }
    }
    private void executeLogin(String username, String password){
        executor.execute(() -> {
            Boolean result = login(username, password);
            Log.d("Login", String.valueOf(result));
            handler.post(() -> {
                if (result){
                    Intent toDash = new Intent(this, Dashboard.class);
                    toDash.putExtra("user", usernameEditText.getText().toString());
                    startActivity(toDash);
                    //finish();
                } else {
                    CharSequence error = "Error login. Check your credentials!";
                    int dur = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, error, dur);
                    toast.show();
                }
            });
        });
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2* hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private Boolean login(String username, String password) {
        String apiUrl = "http://143.198.246.13/motif/api/index.php/user/search?user=";

        Log.d("Username", username);
        Log.d("password", password);

        apiUrl = apiUrl+ username;
        Log.d("Url:", apiUrl);
        String result = "";
        JSONObject jsonResponse;
        JSONArray returnedJSON = null;
        String returnedPass = "";
        String hash = "";
        HttpURLConnection urlConnection;

        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
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
                Log.d("hash", hash.toString());
                
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
            if (result.equals("[]")) {
                return false;
            }
            returnedJSON = new JSONArray(result);
            //Log.d("Result: ", String.valueOf(jsonResponse));

            returnedPass = returnedJSON.getString(0);
            jsonResponse = new JSONObject(returnedPass);
            returnedPass = jsonResponse.getString("password");


            Log.d("returnedPass:", returnedPass);

                

        } catch(IOException e){ //| JSONException
            e.printStackTrace();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //Log.d("LoginHash2", returnedPass.toString());
        return hash.equals(returnedPass);
    }
}