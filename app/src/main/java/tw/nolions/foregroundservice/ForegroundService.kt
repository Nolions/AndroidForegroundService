package tw.nolions.foregroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class ForegroundService : Service() {
    private val CHANNEL = "ForegroundService Kotlin"

    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("contentMsg", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationChannel()

        val contentMsg = intent?.getStringExtra("contentMsg")

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            0
        )

        val switchIntent = Intent(this, SwitchButtonListener::class.java)
        val pendingSwitchIntent = PendingIntent.getBroadcast(
            this, 0,
            switchIntent, 0
        )

        val layout = RemoteViews(packageName, R.layout.notification_view)
        layout.setTextViewText(R.id.notifyTitle, getString(R.string.app_name))
        layout.setTextViewText(R.id.notifyMessage, contentMsg)
        layout.setOnClickPendingIntent(R.id.notifyBtn, pendingSwitchIntent)


        val notification = NotificationCompat.Builder(this, CHANNEL)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentMsg)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(layout)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    class SwitchButtonListener : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
//            Log.d("Here", "I am here")
//            val flashLight: FlashOnOff
//            flashLight = FlashOnOff()
//            flashLight.flashLightOff()
//            flashLight.releaseCamera()
        }
    }
}
