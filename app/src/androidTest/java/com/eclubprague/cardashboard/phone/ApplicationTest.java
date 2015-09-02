package com.eclubprague.cardashboard.phone;

import android.app.Application;
import android.test.ActivityUnitTestCase;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.eclubprague.cardashboard.phone.activities.ScreenSlideActivity;

public class ApplicationTest extends ActivityUnitTestCase<ScreenSlideActivity> {

    public ApplicationTest(){
        super(ScreenSlideActivity.class);
    }

    @SmallTest
    public void testNothing() {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }



}