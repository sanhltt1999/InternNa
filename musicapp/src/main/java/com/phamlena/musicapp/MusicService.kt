package com.phamlena.musicapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.phamlena.musicapp.PlayerActivity.Companion.songPosition

class MusicService : Service() {

    private var myBinder = MyBinder()
    var  mediaPlayer : MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder(){
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn : Int){

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val imArt = PlayerActivity.musicListPA[songPosition].path?.let { getImArt(it) }
        val image = if(imArt!=null){
            BitmapFactory.decodeByteArray(imArt,0, imArt.size)
        }else{
            BitmapFactory.decodeResource(resources, R.drawable.music)
        }

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPA[songPosition].title)
            .setContentText(PlayerActivity.musicListPA[songPosition].artist)
            .setSmallIcon(R.drawable.playlist)
            .setLargeIcon(image)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setColor(
                ContextCompat.getColor(applicationContext,
                R.color.white))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Pause", playPendingIntent)
            .addAction(R.drawable.next, "Next", nextPendingIntent)
            .addAction(R.drawable.close, "Exit", exitPendingIntent)
            .build()
        startForeground(13, notification)

    }

    fun createMediaPlayer(){
        try {
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer()
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(PlayerActivity.musicListPA[songPosition].path)
            mediaPlayer?.prepare()
            PlayerActivity.isPlaying = true
            PlayerActivity.binding.ibPause.setImageResource(R.drawable.pause)
            showNotification(R.drawable.pause)

        }catch (e:Exception) {
            return
        }
    }

    fun seekBarSetup(){
        runnable = Runnable {
            PlayerActivity.binding.seekbarStart.text = formatDuration(mediaPlayer?.currentPosition!!.toInt())
            PlayerActivity.binding.seekbar.progress = mediaPlayer?.currentPosition!!
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
    }

}