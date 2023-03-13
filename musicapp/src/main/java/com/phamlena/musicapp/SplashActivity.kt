package com.phamlena.musicapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File

class SplashActivity() : AppCompatActivity() {
    var list : ArrayList<Music> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Observable.create<ArrayList<Music>> {
            getM()
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Handler(Looper.getMainLooper()).postDelayed({
                    MusicListSingleton.getInstance().setMusicList(list)
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                }, 3000)

            }
            ) { t: Throwable? ->

            }

    }

    fun getM(): ArrayList<Music>{
        val temList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID)
        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, MediaStore.Audio.Media.DATE_ADDED + " DESC", null)
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val albumID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUri = Uri.withAppendedPath(uri,albumID).toString()
                    if(duration >0){
                        val music = Music(artUri, artist, duration, id, path, title)
                        val file = music.path?.let { File(it) }
                        if (file != null) {
                            if(file.exists())
                                temList.add(music)
                        }
                    }

                }while (cursor.moveToNext())
            }
            cursor.close()
        }
        list = temList
        return temList
    }
}


