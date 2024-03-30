package com.codesteem.mylauncher // The package name for the launcher app

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

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
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Verify that the package name of the app context matches the expected package name
        assertPackageName("com.codesteem.mylauncher", appContext)
    }

    /**
     * Assert that the package name of the given context matches the expected package name.
     */
    private fun assertPackageName(expectedPackageName: String, context: Context) {
        assertEquals(expectedPackageName, context.packageName)
    }
}
