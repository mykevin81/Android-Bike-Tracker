package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;



public class TrackerActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        final Button start_pause = (Button) findViewById(R.id.start_pause_btn);
        final String Pause_String = getResources().getString(R.string.Pause_Button);

        start_pause.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Chronometer Current_Time = (Chronometer) findViewById(R.id.timer);
                Current_Time.setBase(SystemClock.elapsedRealtime());

                if (start_pause.getText() != Pause_String) {

                    //change Start button text to pause
                    start_pause.setText(Pause_String);

                    //Start the timer

                    Current_Time.start();

                } else {
                    start_pause.setText(R.string.Resume_Button);
                    Current_Time.stop();
                }

            }


        });



    }










}
