package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * Created by crua9 on 4/12/2015.
 */
public class Help extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        /**
         * I'm just blocking this section out so you can test it as needed. There is a ton of errors in this section, and I don't have time to fix them right now.
         *
         * If you want to work on them, please feel free to take a look at the notes at the bottom.
         *

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        ToggleButton tts = (ToggleButton) findViewById(R.id.tts);
        Button submit = (Button) findViewById(R.id.subit);
        Button start = (Button) findViewById(R.id.button);


        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                startActivity(new Intent(c, TrackerActivity.class));
                finish();

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Data.Spped, getdata(R.id.speed));
                editor.putString(Data.TTS, getdata(R.id.tts));
                editor.commit();
                finish();
            }
        });


    }


    String getdata(int id)
    {
        ToggleButton speed = (ToggleButton) findViewById(R.id.speed);
        return speed.getText().toString();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String speedc =preferences.getString(Data.Spped, Data.default_Speed);
        String ttsc = preferences.getString(Data.TTS, Data.default_TTS);

        settext(R.id.tts, ttsc);
        settext(R.id.speed, speedc);
    }

    public void settext(int id, String text) {

        ToggleButton txt = (ToggleButton) findViewById(id);
        if(text!="")
            txt.setText(text);
    }

});*/

    }}


/**
 *
 * I basically copy and modify from one of my other projects (disability card).
 * That project (which is on GitHUb. will massivly help us with this section.
 *
 * Basically, we need the string to change when the person touches the toggle button and presses submit
 * When they press the speed button, it should go between MPH and KM. From there, the MPH or KM
 * should be added/changed ona string. This string can be used in other areas to allow the app to know if
 * the user wants their read out on speed to be.
 * This can be done with a simple if else
 *
 * Some with the TTS. But, it will basically turn it on or off.
 *
 *
 * BTW, this section is no where near being done. Since I never really coded for toggle buttons before, this code may have to be modify a bit.
 */
