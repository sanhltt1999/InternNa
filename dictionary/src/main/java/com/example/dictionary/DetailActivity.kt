package com.example.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageButton
import android.widget.TextView
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var ttsUK : TextToSpeech
    private lateinit var ttsUS : TextToSpeech
    lateinit var tvEnWord : TextView
    lateinit var tvMean : TextView
    lateinit var tvSyn : TextView
    lateinit var ibSpeakUK : ImageButton
    lateinit var ibSpeakUS : ImageButton
    lateinit var ibFavorite : ImageButton
    private lateinit var databaseAccess: DatabaseAccess
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        init()
        val word = intent.getParcelableExtra<Word>("word")

        if(word != null){

            tvEnWord.text = word.en_Word
            tvMean.text = word.meaning
            tvSyn.text = word.syn
            if(word.favorite == 1){
                ibFavorite.setImageResource(R.drawable.ic_favorite_blue)
            }else{
                ibFavorite.setImageResource(R.drawable.ic_heart)

            }
        }
        initTts()

        ibSpeakUK.setOnClickListener {
            ttsUK.speak(tvEnWord.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
        }
        ibSpeakUS.setOnClickListener {
            ttsUS.speak(tvEnWord.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
        }

        databaseAccess = DatabaseAccess(this)

        ibFavorite.setOnClickListener {
            if (word != null) {
                if (word.favorite == 0 || word.favorite == null) {
                    addFavorite(word)
                    ibFavorite.setImageResource(R.drawable.ic_favorite_blue)
                } else {
                    word.favorite = 0
                    databaseAccess.update(word)
                    ibFavorite.setImageResource(R.drawable.ic_heart)

                }
            }
        }
    }

    fun init(){
        tvEnWord = findViewById(R.id.tvEnWOrd)
        tvMean  = findViewById(R.id.tv_content_meaning)
        tvSyn = findViewById(R.id.tv_content_syn)
        ibSpeakUK = findViewById(R.id.iv_loud_uk)
        ibSpeakUS = findViewById(R.id.iv_loud_us)
        ibFavorite = findViewById(R.id.ibFavorite)

    }

    fun initTts(){
        ttsUK = TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                ttsUK.language = Locale.UK
            }
        }
        ttsUS = TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                ttsUS.language = Locale.US
            }
        }
    }

    fun addFavorite(word: Word){
        word.favorite = 1
        databaseAccess.update(word)
    }

}