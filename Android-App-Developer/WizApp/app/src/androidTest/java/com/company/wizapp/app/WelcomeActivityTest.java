package com.company.wizapp.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WelcomeActivityTest {

    @Rule
    public ActivityTestRule<WelcomeActivity> activityRule =
            new ActivityTestRule<>(WelcomeActivity.class);

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withText("GET STARTED"))
                .check(matches(isDisplayed()));
    }
}
