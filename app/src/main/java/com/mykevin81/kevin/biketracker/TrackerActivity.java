package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;



//TODO wireframe setting menu


public class TrackerActivity extends Activity{


    GoogleMap mMap;
    public Chronometer Timer;
    private boolean isPaused = false;
    public long time = 0;
    public long timeWhenStopped = 0;


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
        Timer.setBase(SystemClock.elapsedRealtime());
        time = Timer.getBase();
        Timer.start();
        isPaused = false;

        //TODO Start tracking real time location on map
    }

    private void stopTimer() {
        Timer.stop();
        Timer.setBase(SystemClock.elapsedRealtime());
        time = 0;
        isPaused = false;
    }

    private void pauseTimer() {
        timeWhenStopped = SystemClock.elapsedRealtime() - time;
        Timer.stop();
        Timer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        isPaused = true;
    }

    private void resumeTimer() {
        Timer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        time = Timer.getBase();
        Timer.start();
        isPaused = false;

        //TODO Start tracking real time location on map
    }

    private boolean isPaused() {
        return isPaused;
    }

    private void setMapUI(){
        mMap.setMyLocationEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to north
                    .tilt(0)                   // Sets the tilt of the camera to 0 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }


}


