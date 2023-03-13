package com.phamlena.musicapp

import MusicAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.phamlena.musicapp.databinding.FragmentOnlineMusicBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OnlineMusicFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentOnlineMusicBinding
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var reference: DatabaseReference

    var musicListOnline: ArrayList<Music> = arrayListOf()

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
        binding = FragmentOnlineMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSongFromFirebase()
        binding.shuffleBtn.setOnClickListener {
            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class", "OnlineShuffle")
            startActivity(intent)
        }
    }

    private fun getSongFromFirebase() {
        binding.rvMusic.setHasFixedSize(true)
        binding.rvMusic.setItemViewCacheSize(13)
        binding.rvMusic.layoutManager = LinearLayoutManager(context)
        musicAdapter = MusicAdapter(
            this@OnlineMusicFragment.requireContext(),
            musicListOnline,
            object : MusicAdapter.IClickSong {
                override fun playMusic(position: Int) {
                    val intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra("index", position)
                    intent.putExtra("class", "OnlineMusic")
                    activity?.let { ContextCompat.startActivity(it, intent, null) }
                }

            }
        )
        binding.rvMusic.adapter = musicAdapter
        binding.tvTotal.text = "Total Songs: " + musicAdapter.itemCount
        reference = FirebaseDatabase.getInstance().getReference("music")
        reference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                return Transaction.success(currentData)
            }
            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                currentData?.let {
                    for (dataSnapshot in it.children) {
                        val music = dataSnapshot.getValue(Music::class.java)
                        musicListOnline.add(music ?: Music())
                    }
                    musicAdapter.setList(musicListOnline)
                }
            }
        })

    }
}