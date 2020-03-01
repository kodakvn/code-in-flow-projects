package com.kodak.example.intentserviceexample

import android.app.IntentService
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat

class ExampleIntentService : IntentService("ExampleIntentService") {

    companion object {
        private val TAG = "kkk"
    }

    lateinit var wakeLock: PowerManager.WakeLock

    init {
        setIntentRedelivery(true)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ExampleApp:WakeLock")
        wakeLock.acquire(600000)
        Log.d(TAG, "Wakelock acquired")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Example Intent service")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_android)
                .build()

            startForeground(1, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

        wakeLock.release()
        Log.e(TAG, "Wakelock released")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent")

        val input = intent?.getStringExtra("inputExtra")
        if (input != null) {
            for (i in 0..9) {
                Log.d(TAG, input + " - " + i)
                SystemClock.sleep(1000)
            }
        }
    }
}