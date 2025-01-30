package com.example.myapplication


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.ActivityTransitionResult

@SuppressLint("InlinedApi")
@Composable
fun UserActivityBroadcastReceiver(
    systemAction: String,
    systemEvent: (userActivity: String) -> Unit,
) {
    val context = LocalContext.current
    val currentSystemOnEvent by rememberUpdatedState(systemEvent)

    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(TRANSITIONS_RECEIVER_ACTION)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val result = intent?.let { ActivityTransitionResult.extractResult(it) } ?: return
                var resultStr = ""
                for (event in result.transitionEvents) {
                    resultStr += "${getActivityType(event.activityType)} " +
                            "- ${getTransitionType(event.transitionType)}"
                }
                Log.d("UserActivityReceiver", "onReceive: $resultStr")
                currentSystemOnEvent(resultStr)
            }
        }
        context.registerReceiver(broadcast, intentFilter, RECEIVER_NOT_EXPORTED)
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}