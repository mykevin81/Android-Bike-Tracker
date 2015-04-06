package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;



public class TrackerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        //always show the current time in this box
        final Chronometer Current_Time = (Chronometer) findViewById(R.id.time);
        Current_Time.setBase(SystemClock.elapsedRealtime());
        Current_Time.start();


    }










}
