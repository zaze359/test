package com.zaze;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.text.TextUtils;

import com.zaze.model.entity.TableEntity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-20 - 18:29
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTestRule {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test() {
//        onView(withId(R.id.main_test_tv)).perform(click(), replaceText("replace"));
//        onView(withId(R.id.main_test_tv)).check(matches(withText("test")));
    }

    @Test
    public void testData() {
        onData(allOf(is(instanceOf(TableEntity.class)), tableEntityItemWithName("aa")));
    }

    public static Matcher<Object> tableEntityItemWithName(final String name) {
        return new BoundedMatcher<Object, TableEntity>(TableEntity.class) {
            @Override
            protected boolean matchesSafely(TableEntity item) {
                return item != null && !TextUtils.isEmpty(name) && (item.getName().equals(name));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item name : " + name);
            }
        };
    }

    @Test
    public void testBtn() throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().resourceId("com.zaze:id/main_test_button")
                .className("android.widget.Button"));
//        uiObject.setText("aaaa");
        sleep(1000);
        uiObject.click();
        sleep(3000);
    }

    @Test
    public void testItem() throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().resourceId("com.zaze:id/item_table_name")
                .className("android.widget.TextView").text("Animation"));
        uiObject.click();
        sleep(3000);
    }

}
