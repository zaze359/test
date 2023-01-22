package com.zaze.demo.compose.data

import androidx.navigation.NavHostController


/**
 * Description :
 * @author : zaze
 * @version : 2023-01-13 01:01
 */
data class Sample(
    val title: String,
    val navigationAction: (NavHostController) -> Unit
)