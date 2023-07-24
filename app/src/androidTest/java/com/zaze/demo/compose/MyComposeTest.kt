package com.zaze.demo.compose

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class MyComposeTest {

    @get:Rule
//    val composeTestRule = createComposeRule()
    val composeTestRule = createAndroidComposeRule<ComposeActivity>()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun show_scaffold_page_when_click_scaffold() {
//        composeTestRule.setContent {
//            val windowSizeClass = calculateWindowSizeClass(activityTestRule.activity)
//            MyAppTheme {
//                MainScreen(uiState = fakeUiState, /*...*/)
//            }
//
//            MyApp(
//                windowSizeClass = windowSizeClass,
//                onAction = {
//                    ActivityUtil.startActivity(this, it)
//                })
//        }
        composeTestRule.onNodeWithText("scaffold").performClick()
        composeTestRule.onNodeWithText("Content").assertIsDisplayed()
        // 输出语义树
        composeTestRule.onRoot().printToLog("TAG")
    }
}