package com.zaze.demo.compose.data

import com.zaze.demo.compose.ui.SampleNavigationActions


/**
 * Description :
 * @author : zaze
 * @version : 2023-01-13 01:01
 */
data class Sample(
    val title: String,
    val navigationAction: (SampleNavigationActions) -> Unit
)