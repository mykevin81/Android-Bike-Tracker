package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button)findViewById(R.id.Start_button);
    /**Enter tracking activity when Start button from Welcome page is pressed */


        final Context c = this;


        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(c, TrackerActivity.class));
                finish();
  /**
   * NOTE FROM crua9
   * Just compare this page to others when you want to remove the actionbar stuff. I moved the detection of the button within the on create, and allowed it to find the class quicker.
   * Check the manifest.
   *
   * BTW, I didn't know if you wanted to keep this class open. If you do, then take off the finish(). This basically kills this class so it doesn't eat up the users memory and battery.
   */
            }});}}
