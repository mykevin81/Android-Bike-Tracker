package com.mykevin81.kevin.biketracker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TrackerActivityTest {

    @Rule
    public final ActivityTestRule<TrackerActivity> trackerActivity = new ActivityTestRule<>(TrackerActivity.class);


}
