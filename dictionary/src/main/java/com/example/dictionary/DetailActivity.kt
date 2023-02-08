package com.example.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val word = intent.getParcelableExtra<Word>("word")
        if(word != null){
            val tvEnWord : TextView = findViewById(R.id.tvEnWOrd)
            val tvMean : TextView = findViewById(R.id.tv_content_meaning)
            val tvSyn : TextView = findViewById(R.id.tv_content_syn)
            tvEnWord.text = word.en_Word
            tvMean.text = word.meaning
            tvSyn.text = word.syn


        }
    }
}