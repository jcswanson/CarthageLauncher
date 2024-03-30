package com.codesteem.mylauncher // The package name for the launcher app

import androidx.test.platform.app.InstrumentationRegistry // Import the InstrumentationRegistry class for accessing the Instrumentation object
import androidx.test.ext.junit.runners.AndroidJUnit4 // Import the AndroidJUnit4 class for running instrumentation tests

import org.junit.Test // Mark the method as a test case
import org.junit.runner.RunWith // Import the RunWith class for specifying the test runner

import org.junit.Assert.* // Import the Assert class for assertion methods

/**
 * Instrumented test, which will execute on an Android device. 
 * 
 * See [testing documentation](http://d.android.com/tools/testing). 
 */
@RunWith(AndroidJUnit4::class) // Specify the AndroidJUnit4 test runner for this test class
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext // Get the Instrumentation object and retrieve the targetContext

        // Verify that the package name of the app context matches the expected package name
        assertEquals("com.codesteem.mylauncher", appContext.packageName)
    }
}
