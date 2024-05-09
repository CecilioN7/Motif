package com.example.motif;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
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
    TextView samplerateview;
    String AudioSavePathInDevice = null;

    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    int sampleRate = 0; // Standard Hz sample rate
    int channel = 0;
    int format = 0;
    int buffer = 0;
    byte[] buffersize;
    short[] audiobuffer = new short[buffer / 2];
    AudioRecord record;
    Thread thread;
    int fnum = 0;
    int bytesRead = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tuner);

        buttonStart = findViewById(R.id.RecordButton);
        buttonStop = findViewById(R.id.StopButton);
        recordedNoteTextView = findViewById(R.id.NoteTextView);
        micStatusTextView = findViewById(R.id.StatusTextView);
        samplerateview = findViewById(R.id.rateTextView);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    // if permission allowed, call the recording function
                    //  micRecording();
                }
            }
        });
        //function for stopping the microphone recording
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop the recording
                if (record != null) {
                    try {
                        micFlag = false;
                        record.stop();
                        //  mediaRecorder.reset();
                        record.release();

                        //   mediaRecorder.release();
                        //    mediaRecorder.reset();
                        //   mediaRecorder.release();
                        record = null;
                        //    mediaRecorder = null;
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


        sampleRate = 44100;
        channel = AudioFormat.CHANNEL_IN_MONO;
        format = AudioFormat.ENCODING_PCM_16BIT;
        buffer = AudioRecord.getMinBufferSize(sampleRate, channel, format);
        record = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channel, format, buffer);


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

                    buffersize = new byte[buffer];//data from audio

                    //int bytesRead = record.read(buffer, 0, buffersize);
                    bytesRead = record.read(buffersize, 0, buffer);
                  //  if (bytesRead > 0) {
                   //     // function call to translation function
                    //    noteTranslate(buffer, bytesRead);
                  // }
                }
            }
        });
        thread.start();
        //if (bytesRead > 0) {
            //     // function call to translation function
                noteTranslate(buffer, bytesRead);
          //   }
    }

    public void noteTranslate(int x, int y) {
        //int listIndex = 0;
        // double frequency = calculateFrequency(audioBuffer, bufferSize);

        //double frequency = 320.0;// test frequency
        int frequency = calculateFrequency(sampleRate, audiobuffer);
        //String[] list = new String[2000];
        //String[] list = {"C", "C#", "D", "D#", "E"};//translated notes go into list, i put sample notes for now
        //samplerateview.setText("in notes translate");
       // String fileName = fnum + ".txt";
        //fnum = fnum + 1;
        String note = noteFrequency(frequency);// pass the frequency recorded
        samplerateview.setText(note);//left off here, run
       // samplerateview.setText(" ");
        //--------------------------------------------------------------------------

       // while (listIndex < list.length && list[listIndex] != null) {
          //  listIndex++;
       // }

      //  if (listIndex == list.length) {
          //  System.err.println("Warning: list array is full. Unable to add more notes.");
          //  return;
       // }

       // list[listIndex] = note;
        //-------------------------------------------------------------------------
       // try {
          //  FileWriter fwrite = new FileWriter(fileName);
          //  BufferedWriter bufferedWriter = new BufferedWriter(fwrite);


          //  for (int i = 0; i < list.length; i++) {
           //     bufferedWriter.write(list[i]);
           //     bufferedWriter.write(' ');
         //   }


          //  bufferedWriter.close();// end write
          //  System.out.println("File writing is done.");
      //  } catch (IOException e) {
        //    System.err.println("Error writing to file: " + e.getMessage());
      //  }
        thread.interrupt();//end tread once done;
    }
    public static int calculateFrequency(int hz, short [] data){
        int count = 0;
        int audiodata = data.length;

        for (int i = 0; i < audiodata-1; i++)
        {
            if ((data[i] > 0 && data[i + 1] <= 0) || (data[i] < 0 && data[i + 1] >= 0))
            {
                count = count + 1;
            }
        }
        float rate= count / 2;
        float time1 = (float)audiodata / (float)hz;
        float frequency = rate/time1;
        return (int)frequency;
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
                // micRecording();
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


