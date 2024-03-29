package com.codesteem.mylauncher.receievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScreenReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SCREEN_STATE_CHANGED = "com.codesteem.mylauncher.ACTION_SCREEN_STATE_CHANGED"
        const val EXTRA_SCREEN_STATE = "screen_state"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                // Screen is turned on
                Log.d("ScreenReceiver", "Screen ON")
                val screenStateChangedIntent = Intent(ACTION_SCREEN_STATE_CHANGED)
                screenStateChangedIntent.putExtra(EXTRA_SCREEN_STATE, true)
                context?.sendBroadcast(screenStateChangedIntent)
                // Add your code to handle screen ON event
            }
            Intent.ACTION_SCREEN_OFF -> {
                // Screen is turned off
                Log.d("ScreenReceiver", "Screen OFF")
                val screenStateChangedIntent = Intent(ACTION_SCREEN_STATE_CHANGED)
                screenStateChangedIntent.putExtra(EXTRA_SCREEN_STATE, false)
                context?.sendBroadcast(screenStateChangedIntent)
                // Add your code to handle screen OFF event
            }
        }
    }
}