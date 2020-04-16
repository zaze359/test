package com.zaze.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-03-24 - 17:41
 */
@RunWith(AndroidJUnit4::class)
class SharedPrefUtilTest {
    lateinit var sp: SharedPrefUtil
    val key = "key"
    val value = "value"

    @Before
    fun useAppContext() { // Context of the app under test.
        sp = SharedPrefUtil.newInstance(ApplicationProvider.getApplicationContext<Context>())
    }

    @Test
    fun contains() {
        sp.sharedPreferences.edit().clear().commit()
        Assert.assertFalse(sp.contains(key))
        sp.commit(key, value)
        Assert.assertTrue(sp.contains(key))
    }

    @Test
    fun apply() {
        sp.sharedPreferences.edit().clear().apply()
        Assert.assertFalse(sp.contains(key))
        sp.apply(key, value)
        Assert.assertEquals(sp[key, ""], value)
    }

    @Test
    fun commit() {
        sp.sharedPreferences.edit().clear().commit()
        Assert.assertFalse(sp.contains(key))
        sp.commit(key, value)
        Assert.assertEquals(sp[key, ""], value)
    }

}