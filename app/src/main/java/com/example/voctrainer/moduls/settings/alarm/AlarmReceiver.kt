package com.example.voctrainer.moduls.settings.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.voctrainer.MainActivity
import com.example.voctrainer.R

class AlarmReceiver:BroadcastReceiver()
{
    private val CHANNEL_ID = "123"
    private val notificationId = 123

    override fun onReceive(context: Context?, intent: Intent?) {

        // Work around:
        // Notification nur starten, wenn Tag in dem Set drin ist...
        // TODO(): Set starten...

        // Create FullscreenIntent
        val fullscreenIntent = Intent(context,MainActivity::class.java)
        val fullscreenPendingIntent = PendingIntent.getActivity(context,0,fullscreenIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        // Create an Notification:
        var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("VocTrainer - Einfach Vokabeln lernen")
            .setContentText("Zeit um deine Vokabeln zu wiederholen :)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)

        // Channel erstellen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "VocTrainer Alarm"
            val descriptionText = "Channel verwaltet die täglichen Erinnerungen zum Lernen"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Add Button to The Activity:
        val resultIntent = Intent(context,MainActivity::class.java)
        val resultPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }
        builder.setContentIntent(resultPendingIntent)
        builder.addAction(R.drawable.ic_launcher_background,"Jetzt Üben",resultPendingIntent)

        // Show the Notification:
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }

        Log.d("VocTrainer","AlarmReceiver - Start Notification...")
    }

}