package com.zaze.utils.ext


import org.junit.Test

/**
 * Description :
 * @author : zaze
 * @version : 2022-12-15 21:59
 */
class HttpExtKtTest {

    @Test
    fun buildGetRequest() {
        var result =
            "https://developer.android.com/jetpack/compose/mental-model?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%23article-https%3A%2F%2Fdeveloper.android.com%2Fjetpack%2Fcompose%2Fmental-model"
                .toHttpUrl()
        println("HttpUrl: $result")
        result =
            "https://developer.android.com:80/jetpack/compose/mental-model?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%23article-https%3A%2F%2Fdeveloper.android.com%2Fjetpack%2Fcompose%2Fmental-model"
                .toHttpUrl()
        println("HttpUrl: $result")
    }
}