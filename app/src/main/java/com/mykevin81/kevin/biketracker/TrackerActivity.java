package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;



public class TrackerActivity extends Activity{

    public Chronometer Timer;
    private boolean isPaused = false;
    public long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        final Button Stop_button = (Button) findViewById(R.id.Stop_btn);
        final Button start_pause = (Button) findViewById(R.id.start_pause_btn);
        final String Pause_String = getResources().getString(R.string.Pause_Button);
        final String Start_String = getResources().getString(R.string.Start_Button);
        final String Resume_String = getResources().getString(R.string.Resume_Button);


        start_pause.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Timer = (Chronometer) findViewById(R.id.timer);
                Timer.setBase(SystemClock.elapsedRealtime());

                if (start_pause.getText() != Pause_String) {

                    //change Start button text to pause
                    start_pause.setText(Pause_String);

                    //Start the timer
                    startTimer();

                } else if(!isPaused && start_pause.getText() == Pause_String) {

                    //change button to resume
                    start_pause.setText(Resume_String);
                    //pause the timer
                    pauseTimer();
                } else {
                    restartTimer();
                }

            }


        });

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
        time = 0;
        Timer.start();
        isPaused = false;
    }

    private void stopTimer() {
        Timer.stop();
        Timer.setBase(SystemClock.elapsedRealtime());
        time = 0;
        isPaused = false;
    }

    private void pauseTimer() {
        time = Timer.getBase();
        Timer.stop();
        Timer.setBase(SystemClock.elapsedRealtime() + time);
        isPaused = true;
    }

    private void restartTimer() {
        Timer.setBase(SystemClock.elapsedRealtime() + time);
        Timer.start();
        isPaused = false;
    }

    private boolean isPaused() {
        return isPaused;
    }

}
