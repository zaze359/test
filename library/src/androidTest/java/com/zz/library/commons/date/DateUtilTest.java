package com.zz.library.commons.date;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.zz.library.commons.log.LogKit;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-26 - 22:16
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DateUtilTest {

    @Test
    public void a() {
        String currentMinAndSec = DateUtil.getMinAndSec(60000L);
        LogKit.v("currentMinAndSec : " + currentMinAndSec);
    }


}