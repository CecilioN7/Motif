package com.example.motif;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

/** @noinspection ALL*/
public class TunerActivity extends AppCompatActivity {

    private static final int writeRequest = 1;
    private boolean micFlag = false;
    private final Handler noteHandler = new Handler(); //test noteTranslate

    Button buttonStart, buttonStop, buttonBack; // Add reference to button
    TextView recordedNoteTextView; // Add reference to the TextView
    TextView micStatusTextView;



    public static final int RequestPermissionCode = 1;

    int sampleRate = 0; // Standard Hz sample rate
    int channel = 0;
    int format = 0;
    int buffer = 256;
    int bytesRead = 0;
    byte[] buffersize;
    short[] audiobuffer = new short[buffer / 2];

    //short[] audiobuffer = {123, -456, 789, 321, -654, 987};
    AudioRecord record;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tuner);

        buttonStart = findViewById(R.id.RecordButton);
        buttonStop = findViewById(R.id.StopButton);
        buttonBack = findViewById(R.id.backButton);
        recordedNoteTextView = findViewById(R.id.NoteTextView);
        micStatusTextView = findViewById(R.id.StatusTextView);



        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(TunerActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        });
        buttonStart.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(TunerActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(TunerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TunerActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);

            } else {
                try {
                    micRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TunerActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //function for stopping the microphone recording
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                //stop the recording
                if (record != null) {
                    try {
                        micFlag = false;
                        record.stop();
                        record.release();
                        record = null;

                        recordedNoteTextView.setText("");
                        Toast.makeText(TunerActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
                        micStatusTextView.setText("Recording ended.");
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }


    // Microphone recording function
    @SuppressLint("SetTextI18n")
    private void micRecording() throws IOException {
        // Check if permission is granted and request the permission for mic and file access
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, writeRequest);
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RequestPermissionCode);
            return;
        }

        //parameters for audio analysis
        sampleRate = 44100;
        channel = AudioFormat.CHANNEL_IN_MONO;
        format = AudioFormat.ENCODING_PCM_16BIT;
        buffer = AudioRecord.getMinBufferSize(sampleRate, channel, format);
        record = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channel, format, buffer);
        audiobuffer = new short[buffer];

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Toast.makeText(this, "Failed to initialize AudioRecord", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            record.startRecording();
            micStatusTextView.setText("Recording...");
            micFlag = true;
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();

            // Start thread for audio processing
            noteThread();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(this, "Recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void noteThread() throws IOException {
        //  }
        noteHandler.postDelayed(() -> {

            try {
                if (micFlag) {
                    noteThread();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 2000);


        thread = new Thread(() -> {
            while (micFlag) {

                buffersize = new byte[buffer];//data from audio
                bytesRead = record.read(buffersize, 0, buffer);


            }
        });
        thread.start();
                noteTranslate(buffer);

    }

    @SuppressLint("SetTextI18n")
    public void noteTranslate(int x) {

        float frequency = calculateFrequency(sampleRate, audiobuffer);


        String note = noteFrequency(frequency);
        recordedNoteTextView.setText("Note Played: " + note);


        thread.interrupt();//end tread once done;
    }
    public static float calculateFrequency(float hz, short [] data){
        int count = 0;
        int audiodata = data.length;

        for (int i = 0; i < audiodata - 1; i++)
        {
            if ((data[i] > 0 && data[i + 1] <= 0) || (data[i] < 0 && data[i + 1] >= 0))
            {
                count = count + 1;
            }
        }
        float rate= (float) count / 2;
        float time1 = (float)audiodata / (float)hz;
        float frequency = rate/time1;
        return (float)frequency;
    }
    public String noteFrequency(double frequency) {

        if (frequency >= 261.63 && frequency < 293.66) {
            return "C";
        } else if (frequency >= 293.66 && frequency < 311.13) {
            return "C#";
        } else if (frequency >= 311.13 && frequency < 329.63) {
            return "D";
        } else if (frequency >= 329.63 && frequency < 349.23) {
            return "D#";
        } else if (frequency >= 349.23 && frequency < 369.99) {
            return "E";
        } else if (frequency >= 369.99 && frequency < 392.00) {
            return "F";
        } else if (frequency >= 392.00 && frequency < 415.30) {
            return "F#";
        } else if (frequency >= 415.30 && frequency < 440.00) {
            return "G";
        } else if (frequency >= 440.00 && frequency < 466.16) {
            return "G#";
        } else if (frequency >= 466.16 && frequency < 493.88) {
            return "A";
        } else if (frequency >= 493.88 && frequency < 523.25) {
            return "A#";
        } else if (frequency >= 523.25 && frequency < 554.37) {
            return "B";
        }

        // If the frequency doesn't match any note, return an empty string or null
        return "None";
    }

    // permission handler
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                try {
                    micRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TunerActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
// end class
}


