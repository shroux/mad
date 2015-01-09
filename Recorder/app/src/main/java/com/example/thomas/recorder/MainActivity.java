package com.example.thomas.recorder;


import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class MainActivity extends Activity {

    MediaRecorder recorder;
    File audiofile = null;
    private static final String TAG = "SoundRecordingActivity";
    private View recordButton;
    private View stopButton;
    private View saveButton;
    private View removeButton;
    private View listButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordButton = findViewById(R.id.record);
        saveButton = findViewById(R.id.save);
        removeButton = findViewById(R.id.remove);
        listButton = findViewById(R.id.list);
        stopButton = findViewById(R.id.stop);
    }

    /**
     * Start the recording
     * @param v
     * @throws IOException
     */
    public void startRecording(View v) throws IOException {
        saveButton.setEnabled(false);
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);
        removeButton.setEnabled(false);

        if (recorder == null)
            recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);
        try
        {
            File tempFile = File.createTempFile("_recorder", ".3gp", getExternalFilesDir(null));
            saveButton.setTag(tempFile);
            removeButton.setTag(tempFile);
            recorder.setOutputFile(tempFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
        }
        catch (IllegalStateException e)
        {
            Log.e(TAG, e.toString());
        }
        catch (IOException e)
        {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Stop Recording
     * @param view
     */
    public void stopRecording(View view) {
        recordButton.setEnabled(false);
        stopButton.setEnabled(false);
        saveButton.setEnabled(true);
        removeButton.setEnabled(true);
        recorder.stop();
        recorder.reset();
    }

    /**
     * Save Recording (internal storage or cloud)
     * @param v
     */
    public void saveRecording(final View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.save, null);
        final EditText nameEditText =
            (EditText) view.findViewById(R.id.name);

        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setView(view);
        inputDialog.setTitle("Choice of name.");
        inputDialog.setPositiveButton("Save",
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    String name = nameEditText.getText().toString().replaceAll(" ", "").trim();

                    if (name.length() != 0)
                    {
                        File appDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Recorder-Beijing");

                        if(!appDir.exists() && !appDir.isDirectory())
                            appDir.mkdirs();

                        File tempFile = (File) v.getTag();
                        File newFile = new File(
                                Environment.getExternalStorageDirectory() + File.separator + "Recorder-Beijing" +
                                        File.separator +
                                        name + ".3gp");
                        tempFile.renameTo(newFile);
                        saveButton.setEnabled(false);
                        removeButton.setEnabled(false);
                        recordButton.setEnabled(true);

                        Toast message = Toast.makeText(MainActivity.this,
                                name + " saved.", Toast.LENGTH_SHORT);
                        message.setGravity(Gravity.CENTER,
                                message.getXOffset() / 2,
                                message.getYOffset() / 2);
                        message.show();


                    }
                    else
                    {
                        Toast message = Toast.makeText(MainActivity.this,
                                "Must have a name.", Toast.LENGTH_SHORT);
                        message.setGravity(Gravity.CENTER,
                                message.getXOffset() / 2,
                                message.getYOffset() / 2);
                        message.show();
                    }
                }
            }
        );
        inputDialog.setNegativeButton("Cancel", null);
        inputDialog.show();
    }

    /**
     * Remove the temp Recording
     * @param v
     */
    public void removeRecording(View v) {
        recordButton.setEnabled(true);
        stopButton.setEnabled(false);
        saveButton.setEnabled(false);
        removeButton.setEnabled(false);
        if (recorder != null)
        {
            recorder.release();
            recorder = null;
            ((File) removeButton.getTag()).delete();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        stopButton.setEnabled(false);
        removeButton.setEnabled(false);
        saveButton.setEnabled(false);
    }


    @Override
    public void onPause()
    {
        super.onPause();

        recordButton.setEnabled(true);
        stopButton.setEnabled(false);
        saveButton.setEnabled(false);
        removeButton.setEnabled(false);
        if (recorder != null)
        {
            recorder.release();
            recorder = null;
            ((File) removeButton.getTag()).delete();
        }
    }

    /**
     * List all recording
     * @param v
     */
    public void listRecording(View v) {
        Intent intent = new Intent(MainActivity.this, AllRecordings.class);
        startActivity(intent);
    }

}