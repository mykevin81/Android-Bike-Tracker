package com.mykevin81.kevin.biketracker;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class TrackerActivityTest {

    @Rule
    public final ActivityTestRule<TrackerActivity> trackerActivity = new ActivityTestRule<>(TrackerActivity.class);


    @Test
    public void startPauseButtonClickTest() {
        onView(withId(R.id.start_pause_btn)).check(ViewAssertions.matches(isClickable()));
    }

    @Test
    public void stopButtonClickTest() {
        onView(withId(R.id.Stop_btn)).check(ViewAssertions.matches(isClickable()));
    }

    @Test
    public void settingButtonClickTest() {
        onView(withId(R.id.SettingButton)).check(ViewAssertions.matches(isClickable()));
    }

    @Test
    public void clockViewExistsTest() {
        onView(withId(R.id.clock)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void timerViewExistsTest() {
        onView(withId(R.id.timer)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void speedViewExistsTest() {
        onView(withId(R.id.tv_speed)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void cadenceViewExistsTest() {
        onView(withId(R.id.tv_cadence)).check(ViewAssertions.matches(isDisplayed()));
    }


}
