package com.codesteem.mylauncher.receievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * This class represents a [BroadcastReceiver] that listens for screen state changes.
 * It is used to detect when the screen is turned on or off and sends a broadcast with the new screen state.
 */
class ScreenReceiver : BroadcastReceiver() {

    /**
     * Companion object containing constants for the action and extra used in the broadcast intent.
     */
    companion object {
        /**
         * The action for the screen state changed broadcast intent.
         */
        const val ACTION_SCREEN_STATE_CHANGED = "com.codesteem.mylauncher.ACTION_SCREEN_STATE_CHANGED"

        /**
         * The extra for the screen state in the broadcast intent.
         */
        const val EXTRA_SCREEN_STATE = "screen_state"
    }

    /**
     * This method is called when the broadcast receiver receives an intent.
     * It checks the action of the intent and logs the screen state accordingly.
     * Then, it sends a broadcast with the new screen state.
     *
     * @param context The [Context] in which the receiver is running.
     * @param intent The [Intent] being received.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                // Screen is turned on
                Log.d("ScreenReceiver", "Screen ON")
                // Send a broadcast with the new screen state
                val screenStateChangedIntent = Intent(ACTION_SCREEN_STATE_CHANGED)
                screenStateChangedIntent.putExtra(EXTRA_SCREEN_STATE, true)
                context?.sendBroadcast(screenStateChangedIntent)
                // Add your code to handle screen ON event
            }
            Intent.ACTION_SCREEN_OFF -> {
                // Screen is turned off
                Log.d("ScreenReceiver", "Screen OFF")
                // Send a broadcast with the new screen state
                val screenStateChangedIntent = Intent(ACTION_SCREEN_STATE_CHANGED)
