package com.xinshu.xinxiaoshu;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by sinyuk on 2017/3/3.
 */
@RunWith(AndroidJUnit4.class)

public class PullDownTest {

    private static final String TAG = "PullDownTest";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.xinshu.xinxiaoshu", appContext.getPackageName());
    }

    @Before
    public void checkCurrentScreen() {
        System.out.println("checkCurrentScreen");
    }

    @Test
    public void test() {
        System.out.println("test");
    }
}
