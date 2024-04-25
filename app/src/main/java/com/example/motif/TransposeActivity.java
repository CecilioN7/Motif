package com.example.motif;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TransposeActivity extends AppCompatActivity {

    private static final int writeRequest = 1;
    private boolean micFlag = false;
    private Handler noteHandler = new Handler(); //test noteTranslate
    private Random randnum = new Random(); //test noteTranslate
    MediaRecorder mediaRecorder; //For mic recording
    Button buttonStart, buttonStop; // Add reference to button
    TextView recordedNoteTextView; // Add reference to the TextView
    TextView micStatusTextView;
    String AudioSavePathInDevice = null;

    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    int samplerate = 0; // Standard Hz sample rate
    int channel =0;
    int format = 0;
    int buffer = 0;
    short[] audiobuffer = new short[buffer / 2];
    AudioRecord record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpose);

        buttonStart = findViewById(R.id.RecordButton);
        buttonStop = findViewById(R.id.StopButton);
        recordedNoteTextView = findViewById(R.id.NoteTextView);
        micStatusTextView = findViewById(R.id.StatusTextView);


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
                if (mediaRecorder != null) {
                    try {
                        micFlag = false;
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        recordedNoteTextView.setText("");
                        Toast.makeText(TransposeActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
                        micStatusTextView.setText("Recording ended.");
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        //set text to display current note
        //String recordedNote = "C#"; //placeholder
        //  recordedNoteTextView.setText("Note Played: " + recordedNote);
    }


    // Microphone recording function
    private void micRecording() {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, writeRequest);
            return;
        }

        // Get the output directory
        File directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String outputPath = directory.getAbsolutePath() + "/mic_recording.mp4";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(outputPath);

        //Set up audio analysis parameters
        samplerate = 44100; // Standard Hz sample rate
        channel = AudioFormat.CHANNEL_IN_MONO;
        format = AudioFormat.ENCODING_PCM_16BIT;
        buffer = AudioRecord.getMinBufferSize(samplerate, channel, format);

        audiobuffer = new short[buffer / 2];
        record = new AudioRecord(MediaRecorder.AudioSource.MIC, samplerate, channel, format, buffer);


        try {
            if (micFlag == false) {
                //isRecording = true;
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(TransposeActivity.this, "Recording started", Toast.LENGTH_SHORT).show();

                micStatusTextView.setText("Recording...");
                //isRecording = true;
                noteThread(); //while recording, call notesTranslate to indentify notes;
            }
            micFlag = true;
            if (micFlag == true) {
                Toast.makeText(TransposeActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(TransposeActivity.this, "Recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(TransposeActivity.this, "Recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void noteThread() throws IOException {
        //--------------------------------------------------------------------------------------------------
        //Text view test code to see if the function call to notesTranslate will update NotesTextView
        noteHandler.postDelayed(new Runnable() {
            @Override

            public void run() {

               // while (micFlag == true) {
                    String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C"};
                    String randomNote = notes[randnum.nextInt(notes.length)];

                    recordedNoteTextView.setText("Note Played: " + randomNote);  //set text to display current note

                    try {
                        if (micFlag == true) {
                            noteThread();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
          //  }
        }, 2000);

        //--------------------------------------------------------------------------------------------------

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (micFlag) {

                    byte[] buffersize = new byte[buffer];//data from audio

                    //int bytesRead = record.read(buffer, 0, buffersize);
                    int bytesRead = record.read(buffersize,0,buffer);
                    if (bytesRead > 0) {
                        // function call to translation function
                        noteTranslate(buffer, bytesRead);
                    }
                }
            }
        });
        thread.start();
    }

        public void noteTranslate(int x ,int y){

        }


    // permission handler
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
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
// end class
}


