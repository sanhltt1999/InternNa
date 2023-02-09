package com.example.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import java.util.*

class DetailActivity : AppCompatActivity() {
    lateinit var ttsUK : TextToSpeech
    lateinit var ttsUS : TextToSpeech
    lateinit var tvEnWord : TextView
    lateinit var tvMean : TextView
    lateinit var tvSyn : TextView
    lateinit var ibSpeakUK : ImageButton
    lateinit var ibSpeakUS : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        init()
        val word = intent.getParcelableExtra<Word>("word")

        if(word != null){
            tvEnWord.text = word.en_Word
            tvMean.text = word.meaning
            tvSyn.text = word.syn
        }

        initTts()
        ibSpeakUK.setOnClickListener(View.OnClickListener {
            ttsUK.speak(tvEnWord.text.toString(),TextToSpeech.QUEUE_FLUSH, null)
        })
        ibSpeakUS.setOnClickListener(View.OnClickListener {
            ttsUS.speak(tvEnWord.text.toString(),TextToSpeech.QUEUE_FLUSH, null)
        })


    }
    fun init(){
        tvEnWord = findViewById(R.id.tvEnWOrd)
        tvMean  = findViewById(R.id.tv_content_meaning)
        tvSyn = findViewById(R.id.tv_content_syn)
        ibSpeakUK = findViewById(R.id.iv_loud_uk)
        ibSpeakUS = findViewById(R.id.iv_loud_us)
    }
    fun initTts(){
        ttsUK = TextToSpeech(this,TextToSpeech.OnInitListener {
            if (it!= TextToSpeech.ERROR){
                ttsUK.setLanguage(Locale.UK)
            }
        })

        ttsUS = TextToSpeech(this,TextToSpeech.OnInitListener {
            if (it!= TextToSpeech.ERROR){
                ttsUK.setLanguage(Locale.US)
            }
        })
    }


}