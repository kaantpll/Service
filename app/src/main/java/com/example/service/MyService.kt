package com.example.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import com.example.service.Constants.CHANNEL_ID
import com.example.service.Constants.MUSIC_NOTIFICATION_ID

class MyService :Service() {

    private lateinit var musicPlayer : MediaPlayer


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initMusic()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        showNotification()
        if(musicPlayer.isPlaying){
            musicPlayer.stop()
        }
        else{
            musicPlayer.start()

        }
        return START_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.stop()
    }

    private fun showNotification(){
        val notificationIntent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)

        val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentText("Music Player")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(MUSIC_NOTIFICATION_ID,notification)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,"My Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                    NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
    private fun initMusic(){
        musicPlayer = MediaPlayer.create(this,R.raw.music)
        musicPlayer.isLooping = true
        musicPlayer.setVolume(100F,100F)
    }
}