package com.example.thomas.customview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends Activity {

    private ImageView _call;
    private ImageView _lock;
    private TextView _hour;
    private ImageView _text;
    private ImageView _photo;
    private ImageView _internet;
    private ImageView _music;
    private TextView _battery;

    private BroadcastReceiver BatteryInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            _battery.setText(String.valueOf(level) + "%");
        }
    };

    private BroadcastReceiver HourInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            _hour.setGravity(Gravity.CENTER);
            _hour.setText(formattedDate);
        }
    };

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Call button
        _call = (ImageView) findViewById(R.id._call);
        _call.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Call is clicked", Toast.LENGTH_LONG).show();

            }
        });

        //lock button
        _lock = (ImageView) findViewById(R.id._lock);
        _lock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Lock is clicked", Toast.LENGTH_LONG).show();

            }
        });

        //Hour Text
        _hour = (TextView) findViewById(R.id._hour);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());
        _hour.setGravity(Gravity.CENTER);
        _hour.setText(formattedDate);
        _hour.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Date is clicked", Toast.LENGTH_LONG).show();

            }
        });
        registerReceiver(this.HourInfoReceiver,
                new IntentFilter(Intent.ACTION_TIME_TICK));

        //text button
        _text = (ImageView) findViewById(R.id._text);
        _text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Text is clicked", Toast.LENGTH_LONG).show();

            }
        });

        //photo button
        _photo = (ImageView) findViewById(R.id._photo);
        _photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Photo is clicked", Toast.LENGTH_LONG).show();

            }
        });

        //internet button
        _internet = (ImageView) findViewById(R.id._internet);
        _internet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Internet is clicked", Toast.LENGTH_LONG).show();

            }
        });

        //Music button
        _music = (ImageView) findViewById(R.id._music);
        _music.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Music is clicked", Toast.LENGTH_LONG).show();

            }
        });


        //Battery
        _battery = (TextView) findViewById(R.id._battery);
        _battery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Battery is clicked", Toast.LENGTH_LONG).show();
            }
        });
        registerReceiver(this.BatteryInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(this.BatteryInfoReceiver);
        unregisterReceiver(this.HourInfoReceiver);
        super.onDestroy();
    }
}
