package com.smarterp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class for SmartERP.
 * Hilt needs this to generate the base dagger components.
 */
@HiltAndroidApp
class SmartERPApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any third-party libraries (e.g. Firebase, Timber) here
    }
}
