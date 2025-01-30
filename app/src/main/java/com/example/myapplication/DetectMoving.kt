package com.example.myapplication

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DetectMoving() {
    RequestActivityRecognitionPermission()

    val context = LocalContext.current
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        DetectMovingContent()
    } else {
        Column {
            Text("Activity permission is required")
        }
    }
}

@Composable
fun DetectMovingContent() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val manager = remember {
        UserActivityTransitionManager(context)
    }

    val currentActivity = remember {
        mutableStateOf("STILL")
    }

    val trackingActivity = remember {
        mutableStateOf(false)
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        val response = manager.registerActivityTransitions()
        trackingActivity.value = response.isSuccess
        onDispose {
            scope.launch(Dispatchers.IO) {
                manager.deregisterActivityTransitions()
            }
        }
    }

    UserActivityBroadcastReceiver(TRANSITIONS_RECEIVER_ACTION) {
        currentActivity.value = it
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (currentActivity.value.isNotBlank()) {
            Text(
                text = "CurrentActivity is = ${currentActivity.value}",
            )
        }
        Text(if (trackingActivity.value) "Tracking Activity" else "Not Tracking Activity")
    }
}