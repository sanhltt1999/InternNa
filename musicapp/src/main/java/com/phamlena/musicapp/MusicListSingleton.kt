package com.phamlena.musicapp

class MusicListSingleton {

    private var musicList: ArrayList<Music> = ArrayList<Music>()

    private object Holder {
        val INSTANCE = MusicListSingleton() }

    companion object{
        fun getInstance():MusicListSingleton{
            return Holder.INSTANCE
        }
    }

    fun getMusicList(): ArrayList<Music>{
        return musicList
    }

    fun setMusicList(musicListA: ArrayList<Music>){
        musicList = musicListA
    }
}

