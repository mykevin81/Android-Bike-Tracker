package com.mykevin81.kevin.biketracker;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
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
}
