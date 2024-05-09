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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TunerActivity extends AppCompatActivity {

    private static final int writeRequest = 1;
    private boolean micFlag = false;
    private final Handler noteHandler = new Handler(); //test noteTranslate
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
    int buffer = 0 ;
    short[] audiobuffer = new short[buffer / 2];
    AudioRecord record;
    Thread thread;
    int fnum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tuner);

        buttonStart = findViewById(R.id.RecordButton);
        buttonStop = findViewById(R.id.StopButton);
        recordedNoteTextView = findViewById(R.id.NoteTextView);
        micStatusTextView = findViewById(R.id.StatusTextView);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(TunerActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(TunerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TunerActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);

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
                        Toast.makeText(TunerActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
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
        samplerate = 44100; // Standard Hz sample rate, 44000
        channel = AudioFormat.CHANNEL_IN_MONO;
        format = AudioFormat.ENCODING_PCM_16BIT;
        buffer = 4 * AudioRecord.getMinBufferSize(samplerate, channel, format);

        audiobuffer = new short[buffer / 2];
        record = new AudioRecord(MediaRecorder.AudioSource.MIC, samplerate, channel, format, buffer);


        try {
            if (micFlag == false) {
                //isRecording = true;
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(TunerActivity.this, "Recording started", Toast.LENGTH_SHORT).show();

                micStatusTextView.setText("Recording...");
                //isRecording = true;
                noteThread(); //while recording, call notesTranslate to indentify notes;
            }
            micFlag = true;
            if (micFlag == true) {
                Toast.makeText(TunerActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(TunerActivity.this, "Recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(TunerActivity.this, "Recording failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void noteThread() throws IOException {
        //--------------------------------------------------------------------------------------------------
        //Text view test code to see if the function call to notesTranslate will update NotesTextView
        //Remove when notesTranslate is functional
        noteHandler.postDelayed(new Runnable() {
            @Override

            public void run() {

               // while (micFlag == true) {
                    String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C"};
                    String randomNote = notes[randnum.nextInt(notes.length)];

                    recordedNoteTextView.setText("Note Played: " + randomNote);  //set text to display current note

                    try {
                        if (micFlag) {
                            noteThread();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
          //  }
        }, 2000);

        //--------------------------------------------------------------------------------------------------

        thread = new Thread(new Runnable() {
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
            int listIndex = 0;
            // double frequency = calculateFrequency(audioBuffer, bufferSize);
            double frequency = 320.0;// test frequency
            //String[] list = new String[2000];
            String[] list = {"C", "C#", "D", "D#", "E"};//translated notes go into list, i put sample notes for now

            String fileName = fnum + ".txt";
            fnum = fnum + 1;
            String note = noteFrequency(frequency);// pass the frequency recorded
            //--------------------------------------------------------------------------

            while (listIndex < list.length && list[listIndex] != null) {
                listIndex++;
            }

            if (listIndex == list.length) {
                System.err.println("Warning: list array is full. Unable to add more notes.");
                return;
            }

            list[listIndex] = note;
            //-------------------------------------------------------------------------
            try {
                FileWriter fwrite = new FileWriter(fileName);
                BufferedWriter bufferedWriter = new BufferedWriter(fwrite);


                for (int i = 0; i < list.length; i++) {
                    bufferedWriter.write(list[i]);
                    bufferedWriter.write(' ');
                }


                bufferedWriter.close();// end write
                System.out.println("File writing is done.");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        thread.interrupt();//end tread once done;
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
        return "";
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


