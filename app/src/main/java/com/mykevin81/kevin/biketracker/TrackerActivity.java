package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

//import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;


public class TrackerActivity extends Activity{


    GoogleMap mMap;
    //LatLng myLocation;
    public Chronometer Timer;
    private boolean isPaused = false;
    public long time;
    String TimerTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        final Button Stop_button = (Button) findViewById(R.id.Stop_btn);
        final Button start_pause = (Button) findViewById(R.id.start_pause_btn);
        final String Pause_String = getResources().getString(R.string.Pause_Button);
        final String Start_String = getResources().getString(R.string.Start_Button);
        final String Resume_String = getResources().getString(R.string.Resume_Button);

        //initialize map stuff
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        setMapUI();

        //Start-Pause button Action
        start_pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Timer = (Chronometer) findViewById(R.id.timer);
                Timer.setBase(SystemClock.elapsedRealtime());

                if (start_pause.getText() == Start_String) {

                    //change Start button text to pause
                    start_pause.setText(Pause_String);

                    //Start the timer
                    startTimer();

                } else if(!isPaused() && start_pause.getText() == Pause_String) {

                    //change pause button to resume
                    start_pause.setText(Resume_String);
                    //pause the timer
                    pauseTimer();
                } else {
                    //change the resume button to pause
                    start_pause.setText(Pause_String);
                    resumeTimer();
                }

            }


        });

        //Stop Button Action
        Stop_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Stop the timer
                stopTimer();
                //set start_puase button text back to start
                start_pause.setText(Start_String);
            }

        });


    }



    private void startTimer() {
        Timer.start();
        time = 0;
        isPaused = false;
    }

    private void stopTimer() {
        Timer.stop();
        Timer.setBase(SystemClock.elapsedRealtime());
        time = 0;
        isPaused = false;
    }

    private void pauseTimer() {
        time = Timer.getBase() - SystemClock.elapsedRealtime();
        Log.d(TimerTag, "Pause Time value: " + time);
        Log.d(TimerTag, "Elapsed Time value:" + SystemClock.elapsedRealtime());
        Timer.stop();
        Timer.setBase(time);
        isPaused = true;
    }

    private void resumeTimer() {
        Timer.setBase(SystemClock.elapsedRealtime() + time);
        time = SystemClock.elapsedRealtime() + time;
        Log.d(TimerTag, "Resume Time value: " + time);
        Timer.start();
        isPaused = false;
    }

    private boolean isPaused() {
        return isPaused;
    }

    private void setMapUI(){
        mMap.setMyLocationEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);

    }


}


