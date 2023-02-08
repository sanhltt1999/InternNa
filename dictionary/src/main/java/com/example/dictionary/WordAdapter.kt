package com.example.dictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class WordAdapter (private val words : ArrayList<Word>) : RecyclerView.Adapter<WordAdapter.WordViewHolder>(){

    var onWordClick : ((Word) -> Unit)? = null

    class WordViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvWord : TextView = itemView.findViewById(R.id.tvWord)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.word_layout, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val currentItem = words[position]
        holder.tvWord.text = currentItem.en_Word
        holder.tvWord.setOnClickListener{
            onWordClick?.invoke(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return words.size
    }

}