package com.mykevin81.kevin.biketracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by crua9 on 4/12/2015.
 */
public class Splash extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final Context c = this;
        Thread time = new Thread(){
            public void run(){
                try{
                    /**
                     * change the sleep time for how long you want the splash image to stay up.
                     */
                    sleep(2000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent openStartingPoint = new Intent(c,MainActivity.class);
                    startActivity(openStartingPoint);
                }
            }};
        time.start();
    }
    protected void onPause(){
        super.onPause();
        finish();
    }

}