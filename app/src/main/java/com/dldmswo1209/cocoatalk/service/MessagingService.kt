package com.dldmswo1209.cocoatalk.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.retrofitAPI.MyApi
import com.dldmswo1209.cocoatalk.retrofitAPI.RetrofitInstance
import com.dldmswo1209.cocoatalk.viewController.MainActivity
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService: FirebaseMessagingService() {

    companion object{
        const val CHANNEL_ID = "cocoa"
        const val CHANNEL_NAME = "cocoaTalk"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // 메시지 처리
        // 수신한 메시지를 처리
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        var builder : NotificationCompat.Builder

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(notificationManager.getNotificationChannel(CHANNEL_ID) == null){
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
            builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        }else{
            builder = NotificationCompat.Builder(applicationContext)
        }

        val title = message.notification?.title
        val body = message.notification?.body

        builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.kakao)

        val notification = builder.build()
        notificationManager.notify(1, notification)
    }
}