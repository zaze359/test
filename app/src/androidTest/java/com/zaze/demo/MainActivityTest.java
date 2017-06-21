package com.zaze.demo;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zaze.demo.kotlin.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-20 - 16:19
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(HomeActivity.class);

    @Test
    public void testTextViewDisplay() {
        onView(ViewMatchers.withId(com.zaze.demo.R.id.main_test_button)).check(ViewAssertions.matches(isDisplayed()));
    }
}