package com.example.myapplication

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

@Composable
fun RequestActivityRecognitionPermission() {
    val context = LocalContext.current
    LaunchedEffect (Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    0
                )
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf("com.google.android.gms.permission.ACTIVITY_RECOGNITION"),
                    0
                )
            }
        }
    }
}