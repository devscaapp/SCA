package com.mca.sca
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "Important Notification"
const val chanelName = "com.mca.sca"

class PushNotificationService : FirebaseMessagingService() {

    var TAG = "MyFirebaseMesaggingService"
    val myMessage = "myMessage"

    override fun onMessageReceived(message: RemoteMessage) {

        Log.d(TAG, "onMessageReceived Called")
        Log.d(TAG, "onMessageReceived: Message is received from: " + message.from)

        // This notification is from remote message to work in foreground
        if (message.notification != null) {
            val title = message.notification!!.title!!
            val body = message.notification!!.body!!

            generateNotification(title, body)
        }
    }
    fun getRemoteView(title: String, message: String) : RemoteViews{
        val remoteView = RemoteViews("com.mca.sca",R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.app_icon)

        return remoteView
    }
    fun generateNotification(title: String, message: String) {

        //create intent because when the user click on notification, the app will open
        val intent = Intent(this, MainActivity::class.java)

        //clear all the activities and put this(MainActivity) at the top priority
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


        //we use this pending activity only once( it will destroy after used once)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        //We use channel id , channel name (after Oreo update)
        //we create notification using NotificationBuilder
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                //set icons,autoCancel , OnlyAlertOnce
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000,1000,1000,1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        builder= builder.setContent(getRemoteView(title,message))

        //notificationManager( Android allows to put notification into the titleBar of your application)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //check user version must be greater than OreoVersion which is Code O(oh not zero)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //create an notificationChannel( all notifications must be assigned to a channel)
            val notificationChannel =
                NotificationChannel(channelId, chanelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        //get notify
        notificationManager.notify(0, builder.build())
    }

}