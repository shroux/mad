package com.example.thomas.recorder;


import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.dropbox.client2.android.*;
import com.dropbox.client2.session.*;
import com.dropbox.client2.*;
import java.io.*;

public class MainActivity extends Activity {

    MediaRecorder recorder;
    File audiofile = null;
    private static final String TAG = "SoundRecordingActivity";
    private View recordButton;
    private View stopButton;
    private View saveButton;
    private View removeButton;
    private View listButton;
    private View loginButton;

    final static private String APP_KEY = "clzd8vz5fzxvqve";
    final static private String APP_SECRET = "0leed7w226g8dwh";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mDBApi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordButton = findViewById(R.id.record);
        saveButton = findViewById(R.id.save);
        removeButton = findViewById(R.id.remove);
        listButton = findViewById(R.id.list);
        stopButton = findViewById(R.id.stop);
        loginButton = findViewById(R.id.login);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
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
        inputDialog.setPositiveButton("Save On Phone",
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

        inputDialog.setNeutralButton("Save On Cloud",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString().replaceAll(" ", "").trim();

                        if (name.length() != 0) {
                            File tempFile = (File) v.getTag();
                            dropBox upload = new dropBox(MainActivity.this, mDBApi, "/MAD-B/", tempFile, name+".3gp");
                            upload.execute();
                        } else {
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

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                loginButton.setEnabled(false);
            } catch (IllegalStateException e) {
                Toast message = Toast.makeText(MainActivity.this, "Error authenticating Dropbox", Toast.LENGTH_SHORT);
            }
        }
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
        Intent intent = new Intent(MainActivity.this, ListRecording.class);
        startActivity(intent);
    }

    public void loginDrop(View v) {
        if (!mDBApi.getSession().isLinked())
            mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
    }

}

