package com.example.thomas.recorder;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class ListRecording extends ListActivity {

    private nodeAdapter nodeadapter;
    private static final String TAG = ListRecording.class.getName();
    private MediaPlayer mp;
    private SeekBar sbar;
    private Handler handler_sickBar;
    private Button play;
    private Button pause;
    private TextView streamSound;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recording);
        handler_sickBar = new Handler();
        sbar = (SeekBar) findViewById(R.id.seekBarId);
        sbar.setOnSeekBarChangeListener(progressListner);
        play = (Button) findViewById(R.id.playS);
        pause = (Button) findViewById(R.id.pauseS);
        streamSound = (TextView) findViewById(R.id.name);
        lv = getListView();
        ArrayList<String> myData = new ArrayList<String>();
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Recorder-Beijing");
        String[] files = fileDir.list();
        if(fileDir.exists() && fileDir.isDirectory() && files.length != 0)
        for (int i = 0; i < files.length; i++) {
            myData.add(files[i]);
        }
        nodeadapter = new nodeAdapter(this, myData);
        lv.setAdapter(nodeadapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mp = new MediaPlayer();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mp != null)
        {
            handler_sickBar.removeCallbacks(updateCursor);
            mp.stop();
            mp.release();
            mp = null;
        }
    }


    /**
     * callbacks change seek bar point
     */
    OnSeekBarChangeListener progressListner = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser)
                mp.seekTo(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /**
     * Runnable Update cursor position
     */
    Runnable updateCursor = new Runnable() {
        @Override
        public void run() {
            if (mp.isPlaying())
            {
                sbar.setProgress(mp.getCurrentPosition());
                handler_sickBar.postDelayed(this, 100);
            }
        }
    };

    /**
     * Play the sound (top button)
     */
    public void playSound(View v) {

        mp.start();
        updateCursor.run();
    }

    /**
     * Pause the sound (top button)
     */
    public void pauseSound(View v) {
        if (mp != null)
            mp.pause();
    }

    public static class S_ViewHolder
    {
        TextView title;
        ImageButton remove;
    }

    public class nodeAdapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;
        public List<String> nodes;

        public nodeAdapter(Context context, List<String> allNodes) {
            super(context, -1, allNodes);
            Collections.sort(allNodes, String.CASE_INSENSITIVE_ORDER);
            nodes = allNodes;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int pos, View v, ViewGroup vg) {
            S_ViewHolder vh;

            if (v == null)
            {
                v = inflater.inflate(R.layout.node, null);
                vh = new S_ViewHolder();
                vh.title = (TextView) v.findViewById(R.id.title);
                vh.remove = (ImageButton) v.findViewById(R.id.remove);
                v.setTag(vh);
            }
            else
                vh = (S_ViewHolder) v.getTag();
            String node = nodes.get(pos);
            vh.title.setText(node);
            vh.remove.setTag(node);
            vh.remove.setOnClickListener(deleteButtonListener);
            return v;
        }

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        handler_sickBar.removeCallbacks(updateCursor);
        TextView titleElem = ((TextView) v.findViewById(R.id.title));
        String filename = titleElem.getText().toString();
        String filePath = Environment.getExternalStorageDirectory() + File.separator + "Recorder-Beijing" + File.separator + filename ;
        streamSound.setText(filename);

        try
        {
            mp.reset();
            mp.setDataSource(filePath);
            mp.prepare();
            sbar.setMax(mp.getDuration());
            sbar.setProgress(0);
            mp.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mpy) {
                        mpy.seekTo(0);
                    } // end method onCompletion
                }
            );
            mp.start();
            updateCursor.run();
            }
         catch (Exception e)
        {
        }
    }

    View.OnClickListener deleteButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            AlertDialog.Builder confirmDialog =
                    new AlertDialog.Builder(ListRecording.this);
            confirmDialog.setTitle("Removing Sound");
            confirmDialog.setMessage("Are you sure ?");

            confirmDialog.setPositiveButton("Remove",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            File fileToDelete = new File(Environment.getExternalStorageDirectory() + File.separator + "Recorder-Beijing"
                            + (String) v.getTag());
                            fileToDelete.delete();
                            nodeadapter.remove((String) v.getTag());
                        }
                    }
            );

            confirmDialog.setNegativeButton("Cancel", null);
            confirmDialog.show();
            }
        };
}
