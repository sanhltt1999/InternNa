package com.phamlena.musicapp

import android.app.Service.STOP_FOREGROUND_DETACH
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS ->{
                prevNextSong(increment = false, context = context!!)
            }
            ApplicationClass.PLAY-> {
                if (PlayerActivity.isPlaying) pauseMusic()
                else playMusic()
            }
            ApplicationClass.EXIT ->{
                PlayerActivity.musicService?.stopForeground(STOP_FOREGROUND_DETACH)
                PlayerActivity.musicService?.mediaPlayer?.release()
                PlayerActivity.musicService = null
                exitProcess(1)
            }
            ApplicationClass.NEXT ->{
                prevNextSong(increment = true, context = context!!)
            }
        }
    }
    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService?.mediaPlayer?.start()
        PlayerActivity.musicService?.showNotification(R.drawable.pause)
        PlayerActivity.binding.ibPause.setImageResource(R.drawable.pause)
    }

    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService?.mediaPlayer?.pause()
        PlayerActivity.musicService?.showNotification(R.drawable.play)
        PlayerActivity.binding.ibPause.setImageResource(R.drawable.play)
    }
    private fun prevNextSong(increment : Boolean, context: Context){
        setSongPosition(increment = increment)
        PlayerActivity.musicService?.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music)).centerCrop()
            .into(PlayerActivity.binding.ivImage)
        PlayerActivity.binding.tvSongName.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        playMusic()


    }
}