package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//TODO change background design

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button)findViewById(R.id.Start_button);
        Button setting = (Button)findViewById(R.id.Setting_Button);

        final Context c = this;


        setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(c, SettingsActivity.class));
                finish();
            }
        });



        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(c, TrackerActivity.class));
                finish();
            }
        });}}

/**Enter tracking activity when Start button from Welcome page is pressed */


/**
 //TODO Include login options for Google+ and/or facebook etc
 I wasn't sure if you wanted a social button based on the to do. If so, the code below is was is needed.
 Just make sure you change the background of the button and the link as needed.

 b1.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent yt = new Intent(Intent.ACTION_VIEW);
yt.setData(Uri.parse("https://www.youtube.com/playlist?list=PLioOFeHwQV5zxRaDpN-hFzvCqrWnSnADt"));
startActivity(yt);
}
});

 */