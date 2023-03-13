package com.phamlena.musicapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.phamlena.musicapp.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener,
    Runnable {
    companion object {

        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        var isPlaying : Boolean = false
        var musicService : MusicService? = null
        lateinit var binding: ActivityPlayerBinding
        var repeat : Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentService = Intent(this, MusicService ::class.java)
        bindService(intentService,this, BIND_AUTO_CREATE)
        startService(intentService)
        initializeLayout()
        binding.ibBack.setOnClickListener {
            finish()
        }

        binding.ibPause.setOnClickListener{
            if(isPlaying)  pauseMusic()
            else playMusic()
        }
        binding.ibPrevious.setOnClickListener{
            prevSong(false)
        }
        binding.ibNext.setOnClickListener{
            prevSong(true)
        }
        binding.seekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) musicService?.mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

        })

        binding.ibRepeat.setOnClickListener{
            if(!repeat){
                repeat = true
                binding.ibRepeat.setColorFilter(ContextCompat.getColor(this,R.color.purple_200))
            }else{
                repeat = false
                binding.ibRepeat.setColorFilter(ContextCompat.getColor(this,R.color.black))
            }
        }
        binding.ibTimer.setOnClickListener {
            showBottomSheetDialog()
        }

    }
    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music)).centerCrop()
            .into(binding.ivImage)
        binding.tvSongName.text = musicListPA[songPosition].title
        if (repeat) binding.ibRepeat.setColorFilter(ContextCompat.getColor(this,R.color.purple_200))
    }
    private fun createMediaPlayer(){
        try {
            if (musicService?.mediaPlayer == null) musicService?.mediaPlayer = MediaPlayer()
            musicService?.mediaPlayer?.reset()
            musicService?.mediaPlayer?.setDataSource(musicListPA[songPosition].path)
            musicService?.mediaPlayer?.prepare()
            musicService?.mediaPlayer?.start()
            startAnimation()
            isPlaying = true
            binding.ibPause.setImageResource(R.drawable.pause)
            musicService?.showNotification(R.drawable.pause)
            binding.seekbarStart.text = formatDuration(musicService?.mediaPlayer?.currentPosition!!
                .toInt())
            binding.seebarEnd.text= formatDuration(musicService?.mediaPlayer?.duration!!.toInt())
            musicService?.mediaPlayer?.setOnCompletionListener(this)
            binding.seekbar.progress = 0
            binding.seekbar.max = musicService?.mediaPlayer?.duration!!
        }catch (e:Exception) {
            return
        }
    }

    private fun initializeLayout(){
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "LocalMusic" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MusicListSingleton.getInstance().getMusicList())
                setLayout()
            }
            "LocalShuffle" ->{
                musicListPA = ArrayList()
                musicListPA.addAll(MusicListSingleton.getInstance().getMusicList())
                musicListPA.shuffle()
                setLayout()
            }
            "OnlineMusic" ->{
//                musicListPA = ArrayList()
//                musicListPA.addAll(OnlineMusicFragment.musicListOnline)
                setLayout()
            }
            "OnlineShuffle" ->{
//                musicListPA = ArrayList()
//                musicListPA.addAll(OnlineMusicFragment.musicListOnline)
                musicListPA.shuffle()
                setLayout()
            }
        }
    }

    private fun playMusic(){
        binding.ibPause.setImageResource(R.drawable.pause)
        musicService?.showNotification(R.drawable.pause)
        isPlaying = true
        musicService?.mediaPlayer?.start()
        startAnimation()
    }

    private fun pauseMusic(){
        binding.ibPause.setImageResource(R.drawable.play)
        musicService?.showNotification(R.drawable.play)
        isPlaying = false
        musicService?.mediaPlayer?.pause()
        stopAnimation()
    }

    private fun prevSong(increment : Boolean){
        if(increment){
            setSongPosition(true)
            setLayout()
            createMediaPlayer()
        }else{
            setSongPosition(false)
            setLayout()
            createMediaPlayer()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
        musicService?.seekBarSetup()


    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
       setSongPosition(increment = true)
        createMediaPlayer()
        try {
            setLayout()
        }catch (e:Exception) {
            return
        }
    }
    private fun startAnimation(){
        val runnable = Runnable {
            binding.ivImage.animate().rotationBy(360F).withEndAction(this@PlayerActivity).setDuration(10000)
                .setInterpolator(LinearInterpolator()).start()
        }
        binding.ivImage.animate().rotationBy(360F).withEndAction(runnable).setDuration(10000)
            .setInterpolator(LinearInterpolator()).start()

    }
    private fun stopAnimation(){
        binding.ivImage.animate().cancel()
    }

    private fun showBottomSheetDialog(){
        val dialog = BottomSheetDialog(this@PlayerActivity)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.show()
        dialog.findViewById<LinearLayout>(R.id.min_15)?.setOnClickListener{
            Toast.makeText(this@PlayerActivity, "Yeah", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min_30)?.setOnClickListener{
            Toast.makeText(this@PlayerActivity, "Yeah", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min_60)?.setOnClickListener{
            Toast.makeText(this@PlayerActivity, "Yeah", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
    }

    override fun run() {
        binding.ivImage.animate().rotationBy(360F).withEndAction(this@PlayerActivity).setDuration(10000)
            .setInterpolator(LinearInterpolator()).start()
    }
}

