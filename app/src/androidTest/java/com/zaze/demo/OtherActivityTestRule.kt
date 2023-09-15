package com.zaze.demo

import android.os.SystemClock
import android.text.TextUtils
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import com.zaze.demo.component.lifecycle.LifecycleActivity
import com.zaze.demo.data.entity.TableEntity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.core.Is
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-20 - 18:29
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class OtherActivityTestRule {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<LifecycleActivity>()

    @Test
    fun test() {
//        onView(withId(R.id.main_test_tv)).perform(click(), replaceText("replace"));
//        onView(withId(R.id.main_test_tv)).check(matches(withText("test")));
    }

    @Test
    fun testData() {
    }
//
//    @Test
//    @Throws(UiObjectNotFoundException::class)
//    fun testBtn() {
//        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
//        val uiObject = mDevice.findObject(
//            UiSelector().resourceId("com.zaze:id/main_test_button")
//                .className("android.widget.Button")
//        )
//        //        uiObject.setText("aaaa");
//        SystemClock.sleep(1000)
//        uiObject.click()
//        SystemClock.sleep(3000)
//    }
//
//    @Test
//    @Throws(UiObjectNotFoundException::class)
//    fun testItem() {
//        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
//        val uiObject = mDevice.findObject(
//            UiSelector().resourceId("com.zaze:id/item_table_name")
//                .className("android.widget.TextView").text("Animation")
//        )
//        uiObject.click()
//        SystemClock.sleep(3000)
//    }
//
//    companion object {
//        fun tableEntityItemWithName(name: String): Matcher<Any> {
//            return object : BoundedMatcher<Any, TableEntity?>(TableEntity::class.java) {
//                override fun matchesSafely(item: TableEntity?): Boolean {
//                    return item != null && !TextUtils.isEmpty(name) && item.name == name
//                }
//
//                override fun describeTo(description: Description) {
//                    description.appendText("item name : $name")
//                }
//            }
//        }
//    }
}