package com.zaze.demo.component.lifecycle

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MyTestSuite {


//    @get:Rule
//    var activityScenarioRule = activityScenarioRule<LifecycleActivity>()
////    @get:Rule
////    var activityScenarioRule = ActivityScenarioRule(LifecycleActivity::class.java)
//
//    @Test
//    fun testEvent() {
//        val scenario = activityScenarioRule.scenario
////        val scenario = launchActivity<LifecycleActivity>()
////        scenario.onActivity { activity ->
////            activity.startActivity(Intent(activity, MainActivity::class.java))
////        }
////        scenario.moveToState(Lifecycle.State.CREATED)
////        val originalActivityState = scenario.state
////        println("originalActivityState: $originalActivityState")
////        assertEquals(scenario.state, Lifecycle.State.CREATED)
//        scenario.recreate()
//    }
}
