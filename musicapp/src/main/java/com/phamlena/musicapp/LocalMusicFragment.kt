package com.phamlena.musicapp

import MusicAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.phamlena.musicapp.databinding.FragmentLocalMusicBinding
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LocalMusicFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var binding: FragmentLocalMusicBinding

    companion object{
        lateinit  var  musicListLC : ArrayList<Music>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentLocalMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLayout()
        binding.shuffleBtn.setOnClickListener{
            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class", "LocalMusic")
            startActivity(intent)
        }
    }
    private fun initializeLayout(){

        musicListLC = MusicListSingleton.getInstance().getMusicList()
        binding.rvMusic.setHasFixedSize(true)
        binding.rvMusic.setItemViewCacheSize(13)
        binding.rvMusic.layoutManager = LinearLayoutManager(context)
        musicAdapter = MusicAdapter(this@LocalMusicFragment.requireContext(), musicListLC, object  : MusicAdapter.IClickSong{
            override fun playMusic(position: Int) {
                val intent = Intent(context, PlayerActivity::class.java)
                intent.putExtra("index", position)
                intent.putExtra("class", "LocalMusic")
                activity?.let { ContextCompat.startActivity(it, intent, null) }
            }

        })
        binding.rvMusic.adapter = musicAdapter
        binding.tvTotal.text = "Total Songs: " + musicAdapter.itemCount
    }
}