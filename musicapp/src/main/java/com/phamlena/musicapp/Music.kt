package com.phamlena.musicapp

import android.media.MediaMetadataRetriever
import java.util.concurrent.TimeUnit

data class Music(val artUri:String? = null,
                 val artist:String? = null,
                 val duration: Int?= null,
                 val id:String? = null ,
                 val path:String? = null,
                 val title:String? = null)

fun formatDuration(duration: Int) : String{
    val minutes = TimeUnit.MINUTES.convert(duration.toLong(), TimeUnit.MILLISECONDS)
    val seconds = TimeUnit.SECONDS.convert(duration.toLong(), TimeUnit.MILLISECONDS) - minutes* TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    return String.format("%02d:%02d", minutes, seconds)

}
fun getImArt(path : String) : ByteArray?{
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

 fun setSongPosition(increment: Boolean){
     if (!PlayerActivity.repeat){
         if(increment){
             if(PlayerActivity.musicListPA.size - 1 == PlayerActivity.songPosition)
                 PlayerActivity.songPosition = 0
             else ++PlayerActivity.songPosition

         }else{
             if(0 == PlayerActivity.songPosition)
                 PlayerActivity.songPosition = PlayerActivity.musicListPA.size -1
             else --PlayerActivity.songPosition
         }
     }

}