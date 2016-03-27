package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc.ICalculatedCadenceReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc.CalculatedAccumulatedDistanceReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc.CalculatedSpeedReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc.IMotionAndSpeedDataReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeSpeedDistancePcc.IRawSpeedAndDistanceDataReceiver;
import com.dsi.ant.plugins.antplus.pcc.defines.BatteryStatus;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusBikeSpdCadCommonPcc.IBatteryStatusReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.ICumulativeOperatingTimeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.IManufacturerAndSerialReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.IVersionAndModelReceiver;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

//TODO wireframe setting menu


public class TrackerActivity extends Activity{


    GoogleMap mMap;
    public Chronometer Timer;
    private boolean isPaused = false;
    public long time = 0;
    public long timeWhenStopped = 0;

    TextView tv_status;

    //Ant+ sensor variable
    AntPlusBikeSpeedDistancePcc bsdPcc = null;
    PccReleaseHandle<AntPlusBikeSpeedDistancePcc> bsdReleaseHandle = null;
    AntPlusBikeCadencePcc bcPcc = null;
    PccReleaseHandle<AntPlusBikeCadencePcc> bcReleaseHandle = null;




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
                //set start_pause button text back to start
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


    //======================== ANT+ Functions ===================

    /**
     * Show the result of the connection between device and sensors
     *
     * @param result                Result of the connected sensor
     * @param resultCode            The result code given by the sensor
     * @param initialDeviceState    status of the device after connection request
     */
    public void onResultReceived(AntPlusBikeSpeedDistancePcc result, RequestAccessResult resultCode, DeviceState initialDeviceState) {

        switch(resultCode) {
            case SUCCESS:
                bsdPcc = result;
                Toast.makeText(TrackerActivity.this, "Successfully Connected: " + result.getDeviceName(), Toast.LENGTH_SHORT).show();
                tv_status.setText(result.getDeviceName() + ": " + initialDeviceState);
                subscribeToEvents();
                break;

            case CHANNEL_NOT_AVAILABLE:
                Toast.makeText(TrackerActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();
                tv_status.setText("Channel not available!");
                break;

            case ADAPTER_NOT_DETECTED:
                Toast.makeText(TrackerActivity.this, "Sensor not found", Toast.LENGTH_SHORT).show();
                tv_status.setText("Sensor not found");
                break;

            case BAD_PARAMS:
                Toast.makeText(TrackerActivity.this, "Bad request parameters", Toast.LENGTH_SHORT).show();
                tv_status.setText("Bad request parameters");
                break;

            case OTHER_FAILURE:
                Toast.makeText(TrackerActivity.this, "Unknown failure. check logcat for details.", Toast.LENGTH_SHORT).show();
                tv_status.setText("OTHER FAILURE");
                break;

            case DEPENDENCY_NOT_INSTALLED:
                Toast.makeText(TrackerActivity.this, "Dependency is not installed!", Toast.LENGTH_SHORT).show();

                //Alert user to install ANT+ app dependencies before using this app
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TrackerActivity.this);
                alertDialogBuilder.setTitle("Missing Dependency");
                alertDialogBuilder.setMessage("The required service: \n"
                        + AntPlusBikeSpeedDistancePcc.getMissingDependencyName()
                        + "\n was not found. You need to install the ANT+ Plugins service or you may need to update your existing version if you already have it. Do you want to launch the Play Store to get it?");
                alertDialogBuilder.setPositiveButton("Go to Store", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startStore = null;
                        startStore = new Intent(Intent.ACTION_VIEW, Uri
                                .parse("market://details?id="
                                        + AntPlusBikeSpeedDistancePcc
                                        .getMissingDependencyPackageName()));
                        startStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        TrackerActivity.this.startActivity(startStore);
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog waitDialog = alertDialogBuilder.create();
                waitDialog.show();
                break;

            case USER_CANCELLED:
                Toast.makeText(TrackerActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                tv_status.setText("Canceled");
                break;

            case UNRECOGNIZED:
                Toast.makeText(TrackerActivity.this, "Failed: UNRECOGNIZED. PluginLib Upgrade Required?", Toast.LENGTH_SHORT).show();
                tv_status.setText("Unrecognized, update may be required");
                break;

            default:
                Toast.makeText(TrackerActivity.this, "Unrecognized result: " + resultCode, Toast.LENGTH_SHORT).show();
                tv_status.setText("Error code: " + resultCode);
                break;
        }
    }

    public void subscribeToEvents() {
        //TODO create subscribe to the events
    }



}


