package com.example.motif;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TransposeActivity extends AppCompatActivity {

    Button buttonStart, buttonStop;
    TextView recordedNoteTextView; // Add reference to the TextView
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpose);

        buttonStart = findViewById(R.id.RecordButton);
        buttonStop = findViewById(R.id.StopButton);
        recordedNoteTextView = findViewById(R.id.NoteTextView);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(TransposeActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(TransposeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TransposeActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
                } else {
                    // if permission allowed, call the recording function
                    micRecording();
                }
            }
        });
        //function for stopping the microphone recording
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop the recording
                Toast.makeText(TransposeActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
                if (mediaRecorder != null) {
                    try {
                        mediaRecorder.stop();
                    } catch (IllegalStateException e) {

                        e.printStackTrace();
                    }
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
            }
        });

        //set text to display current note
        String recordedNote = "C#"; //placeholder
        recordedNoteTextView.setText("Recorded Note: " + recordedNote);
    }



    // Microphone recording function
    private void micRecording() {
        /* mediaRecorder = new MediaRecorder();
         mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
         mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
         mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
       // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
       // mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // if we are just analyzing audio data in real time, we dont need an output file. set to null
        mediaRecorder.setOutputFile("/dev/null");
*/
        String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_audio.mp4";


        File outputFile = new File(outputPath);


        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);


        mediaRecorder.setOutputFile(outputFile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(TransposeActivity.this, "recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(TransposeActivity.this, "recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(TransposeActivity.this, "recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // permission handler
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                micRecording();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}