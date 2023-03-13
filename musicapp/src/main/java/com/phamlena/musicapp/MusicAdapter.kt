import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phamlena.musicapp.Music
import com.phamlena.musicapp.R
import com.phamlena.musicapp.databinding.MusicViewBinding
import com.phamlena.musicapp.formatDuration

class MusicAdapter(private val context: Context, private var musicList:ArrayList<Music>, val iClickSong : IClickSong):
    RecyclerView.Adapter<MusicAdapter.MusicHolder>() {
    class MusicHolder (binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root){
        val title = binding.tvSongName
        val album = binding.tvAlbumName
        val image = binding.imageMV
        val duration = binding.songDuration
        val root = binding.root
    }

    fun setList(music: ArrayList<Music>) {
        musicList = music
        notifyDataSetChanged()
    }

    interface IClickSong{
            fun playMusic(position: Int)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MusicHolder {
        return MusicHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent , false))
    }

    override fun onBindViewHolder(holder: MusicAdapter.MusicHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].artist
        holder.duration.text = musicList[position].duration?.let { formatDuration(it) }
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music)).centerCrop()
            .into(holder.image)
        holder.root.setOnClickListener{
            iClickSong.playMusic(position)
        }
    }

    override fun getItemCount(): Int {
        return  musicList.size
    }
}