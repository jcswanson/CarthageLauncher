package com.codesteem.mylauncher // The package name for the launcher app

import androidx.test.platform.app.InstrumentationRegistry // Import the InstrumentationRegistry class for accessing the Instrumentation object
import androidx.test.ext.junit.runners.AndroidJUnit4 // Import the AndroidJUnit4 class for running instrumentation tests

import org.junit.Test // Mark a method as a test case
import org.junit.runner.RunWith // Mark a class as a test runner

import org.junit.Assert.* // Import the Assert class for assertion methods

/**
 * Instrumented test, which will execute on an Android device. 
 * 
 * See [testing documentation](http://d.android.com/tools/testing). 
 *
 * This class contains an instrumented test for the launcher app. 
 * Instrumented tests are run on an actual Android device or emulator. 
 */
@RunWith(AndroidJUnit4::class) // Indicates that this class uses the AndroidJUnit4 test runner
class ExampleInstrumentedTest {

    /**
     * A test case that checks if the app context is correct.
     * 
     * This test case retrieves the application context and asserts that the package name is "com.codesteem.mylauncher".
     */
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext // Get the Instrumentation object and retrieve the target context

        // Assert that the package name of the app context is "com.codesteem.mylauncher"
        assertEquals("com.codesteem.mylauncher", appContext.packageName)
    }
}
