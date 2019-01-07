package com.zaze.demo;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;


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
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testTextViewDisplay() {
//        onView(ViewMatchers.withId(R.id.main_test_button)).check(ViewAssertions.matches(isDisplayed()));
    }
}