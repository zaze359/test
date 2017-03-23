package com.zaze.demo;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.zaze.demo.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description :
 *
 * @author : ZAZE
  @version : 2016-11-20 - 18:15
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity activity;
    private Button button;

    public MainActivityInstrumentationTest() {
        // 所有的ActivityInstrumentationTestCase2子类都需要调用该父类的super(String)构造方法
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        // getActivity()方法会在开始所有的testCase之前启动相应的Activity
        activity = getActivity();
        button = (Button) activity.findViewById(com.zaze.demo.R.id.main_test_button);
    }

    @Test
    public void test1() {
        // @Test注解表示一个测试用例方法
        assertNotNull("MainActivity is null", activity);
    }

    @Test
    public void test2() {
        // 这里就是我们测试的目标，判断目标控件的text不为空
        String content = button.getText().toString();
        assertNotNull("MainActivity Content is Null", content);
    }
}
