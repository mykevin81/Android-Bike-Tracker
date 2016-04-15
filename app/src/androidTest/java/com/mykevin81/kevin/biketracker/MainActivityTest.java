package com.mykevin81.kevin.biketracker;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldDisplayWelcomeText() {
        onView(withText("Welcome Biker!")).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void startButtonTest() {
        onView(withId(R.id.Start_button)).check(ViewAssertions.matches(isClickable()));
    }

    @Test
    public void settingButtonTest() {
        onView(withId(R.id.Setting_Button)).check(ViewAssertions.matches(isClickable()));
    }
}
