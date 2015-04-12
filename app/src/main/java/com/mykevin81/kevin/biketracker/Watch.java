package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by crua9 on 4/12/2015.
 */
public class Watch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch);

        LinearLayout start = (LinearLayout)findViewById(R.id.watchb);



}}
