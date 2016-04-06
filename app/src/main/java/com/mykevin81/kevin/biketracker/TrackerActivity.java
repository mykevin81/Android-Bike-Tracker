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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
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

import java.math.BigDecimal;
import java.util.EnumSet;

//TODO wireframe setting menu


public class TrackerActivity extends Activity {


    GoogleMap mMap;
    public Chronometer Timer;
    private boolean isPaused = false;
    public long time = 0;
    public long timeWhenStopped = 0;

    TextView tv_status;
    TextView tv_speed;
    TextView tv_cadence;

    //Ant+ sensor variable
    AntPlusBikeSpeedDistancePcc bsdPcc = null;
    PccReleaseHandle<AntPlusBikeSpeedDistancePcc> bsdReleaseHandle = null;
    AntPlusBikeCadencePcc bcPcc = null;
    PccReleaseHandle<AntPlusBikeCadencePcc> bcReleaseHandle = null;

    BigDecimal wheelSize = new BigDecimal(2.095);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        tv_speed = (TextView) findViewById(R.id.tv_speed);
        tv_cadence = (TextView) findViewById(R.id.tv_cadence);

        final Button Stop_button = (Button) findViewById(R.id.Stop_btn);
        final Button start_pause = (Button) findViewById(R.id.start_pause_btn);
        final ImageButton Setting_button = (ImageButton) findViewById(R.id.SettingButton);

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

                } else if (!isPaused() && start_pause.getText() == Pause_String) {

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


        Setting_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(TrackerActivity.this, SettingsActivity.class));
            }
        });

        resetPcc();
    }

    @Override
    protected void onDestroy() {
        bsdReleaseHandle.close();
        if (bcReleaseHandle != null) {
            bcReleaseHandle.close();
        }
        super.onDestroy();
    }


    /**
     * Begin the timer
     */
    private void startTimer() {
        Timer.setBase(SystemClock.elapsedRealtime());
        time = Timer.getBase();
        Timer.start();
        isPaused = false;

        //TODO Start tracking real time location on map
    }

    /**
     * Stop the timer
     */
    private void stopTimer() {
        Timer.stop();
        Timer.setBase(SystemClock.elapsedRealtime());
        time = 0;
        isPaused = false;
    }

    /**
     * Pause the timer
     */
    private void pauseTimer() {
        timeWhenStopped = SystemClock.elapsedRealtime() - time;
        Timer.stop();
        Timer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        isPaused = true;
    }

    /**
     * Resume the timer
     */
    private void resumeTimer() {
        Timer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        time = Timer.getBase();
        Timer.start();
        isPaused = false;

        //TODO Start tracking real time location on map
    }

    /**
     * Check if the timer is paused
     *
     * @return isPaused boolean value based on the timer.
     */
    private boolean isPaused() {
        return isPaused;
    }

    /**
     * Initialize map view information
     */
    private void setMapUI() {
        mMap.setMyLocationEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
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
     * reset the connection if there is an old connection
     */
    private void resetPcc() {

        if (bsdReleaseHandle != null) {
            bsdReleaseHandle.close();
        }

        if (bcReleaseHandle != null) {
            bcReleaseHandle.close();
        }


        bsdReleaseHandle = AntPlusBikeSpeedDistancePcc.requestAccess(this, this,
                mResultReceiver, mDeviceStateChangeReceiver);

    }

    IPluginAccessResultReceiver<AntPlusBikeSpeedDistancePcc> mResultReceiver = new IPluginAccessResultReceiver<AntPlusBikeSpeedDistancePcc>() {
        // Handle the result, connecting to events on success or reporting
        // failure to user.

        @Override
        public void onResultReceived(AntPlusBikeSpeedDistancePcc result, RequestAccessResult resultCode, DeviceState initialDeviceState) {

            Log.d("ANT+", "going in onResultReceived");

            switch (resultCode) {
                case SUCCESS:
                    bsdPcc = result;
                    Toast.makeText(TrackerActivity.this, "Successfully Connected: " + result.getDeviceName(), Toast.LENGTH_SHORT).show();
                    //tv_status.setText(result.getDeviceName() + ": " + initialDeviceState);
                    subscribeToEvents();
                    break;

                case CHANNEL_NOT_AVAILABLE:
                    Toast.makeText(TrackerActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();
                    //tv_status.setText("Channel not available!");
                    break;

                case ADAPTER_NOT_DETECTED:
                    Toast.makeText(TrackerActivity.this, "Sensor not found", Toast.LENGTH_SHORT).show();
                    //tv_status.setText("Sensor not found");
                    break;

                case BAD_PARAMS:
                    Toast.makeText(TrackerActivity.this, "Bad request parameters", Toast.LENGTH_SHORT).show();
                    //tv_status.setText("Bad request parameters");
                    break;

                case OTHER_FAILURE:
                    Toast.makeText(TrackerActivity.this, "Unknown failure. check logcat for details.", Toast.LENGTH_SHORT).show();
                    //tv_status.setText("OTHER FAILURE");
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
                    //tv_status.setText("Cancelled");
                    break;

                case UNRECOGNIZED:
                    Toast.makeText(TrackerActivity.this, "Failed: UNRECOGNIZED. PluginLib Upgrade Required?", Toast.LENGTH_SHORT).show();
                    //tv_status.setText("Unrecognized, update may be required");
                    break;

                default:
                    Toast.makeText(TrackerActivity.this, "Unrecognized result: " + resultCode, Toast.LENGTH_SHORT).show();
                    //tv_status.setText("Error code: " + resultCode);
                    break;
            }
        }
    };

    IDeviceStateChangeReceiver mDeviceStateChangeReceiver = new IDeviceStateChangeReceiver() {

        @Override
        public void onDeviceStateChange(final DeviceState newDeviceState) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tv_status.setText(bsdPcc.getDeviceName() + ": " + newDeviceState);
                    if (newDeviceState == DeviceState.DEAD)
                        bsdPcc = null;
                }
            });
        }
    };

    /**
     * Start the Ant+ Plugin manager when called.
     */
    protected void startAntPlugin() {
        AntPluginPcc.startPluginManagerActivity(this);
    }

    /**
     * Subscribe to the events and set the text to the view on screen
     */
    public void subscribeToEvents() {

        Log.d("ANT+", "going in subscribeToEvents");
        //Speed Receiver
        //TODO add options to change wheel size
        bsdPcc.subscribeCalculatedSpeedEvent(new CalculatedSpeedReceiver(wheelSize) {
            @Override
            public void onNewCalculatedSpeed(long l, EnumSet<EventFlag> enumSet, final BigDecimal calculatedSpeed) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //tv_speed.setText(String.valueOf(calculatedSpeed));
                        tv_speed.setText("RUN!");//TEST
                    }
                });
            }
        });

        //Accumulated Distance Receiver
        bsdPcc.subscribeCalculatedAccumulatedDistanceEvent(new CalculatedAccumulatedDistanceReceiver(wheelSize) {

            @Override
            public void onNewCalculatedAccumulatedDistance(long l, EnumSet<EventFlag> enumSet, final BigDecimal calculatedAccumulatedDistance) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO connect to distance when there is a text view
                    }
                });
            }
        });

        //Raw Speed and Distance Receiver
        bsdPcc.subscribeRawSpeedAndDistanceDataEvent(new IRawSpeedAndDistanceDataReceiver() {
            @Override
            public void onNewRawSpeedAndDistanceData(long l, EnumSet<EventFlag> enumSet, final BigDecimal timestampOfLastEvent, final long cumulativeRevolutions) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO do something....
                    }
                });
            }
        });

        //Speed and cadence Combined sensor receiver
        if (bsdPcc.isSpeedAndCadenceCombinedSensor()) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bcReleaseHandle = AntPlusBikeCadencePcc.requestAccess(
                            TrackerActivity.this,
                            bsdPcc.getAntDeviceNumber(), 0, true,
                            new IPluginAccessResultReceiver<AntPlusBikeCadencePcc>() {
                                @Override
                                public void onResultReceived(AntPlusBikeCadencePcc result, RequestAccessResult resultCode, DeviceState initialDeviceStateCode) {

                                    switch (resultCode) {
                                        case SUCCESS:
                                            bcPcc = result;
                                            bcPcc.subscribeCalculatedCadenceEvent(new ICalculatedCadenceReceiver() {
                                                @Override
                                                public void onNewCalculatedCadence(long estTimestamp, EnumSet<EventFlag> eventFlag, final BigDecimal calculatedCadence) {

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //tv_cadence.setText(String.valueOf(calculatedCadence));
                                                            tv_cadence.setText("ROLL!");
                                                        }
                                                    });
                                                }
                                            });
                                            break;

                                        case CHANNEL_NOT_AVAILABLE:
                                            //Channel not available
                                            break;
                                        case BAD_PARAMS:
                                            //bad params
                                            break;
                                        case OTHER_FAILURE:
                                            //other failure
                                            break;
                                        case DEPENDENCY_NOT_INSTALLED:
                                            //dependencies not installed
                                            break;
                                        default:
                                            //unknown error
                                            break;
                                    }
                                }
                            },
                            new IDeviceStateChangeReceiver() {
                                @Override
                                public void onDeviceStateChange(final DeviceState newDeviceState) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (newDeviceState != DeviceState.TRACKING) {
                                                //tv_cadence.setText(newDeviceState.toString());
                                                tv_cadence.setText("newRoll");
                                            }

                                            if (newDeviceState == DeviceState.DEAD) {
                                                bcPcc = null;
                                            }

                                        }
                                    });
                                }
                            }
                    );
                }
            });
        }//else here


    }

}


