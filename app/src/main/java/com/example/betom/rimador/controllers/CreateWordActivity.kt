package com.example.betom.rimador.controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.adapters.FastScroller
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.utilities.DEFAULT_MAX_SYLLABLES
import com.example.betom.rimador.utilities.DEFAULT_MIN_SYLLABLES
import com.example.betom.rimador.wordHandlers.WordCreator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import kotlinx.android.synthetic.main.activity_create_word.*
import java.util.*
import kotlin.collections.ArrayList

class CreateWordActivity : AppCompatActivity() {

    private lateinit var fastScroller:FastScroller
    private val generatedWords=ArrayList<Word>()
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_word)

        setupAdapters()
    }

    fun generateWordsButtonClicked(view: View){
        val wordCreator= WordCreator()
        val minimum=if(minSyllablesText.text.isNotEmpty())
            minSyllablesText.text.toString().toInt()
        else
            DEFAULT_MIN_SYLLABLES

        val maximum=if(maxSyllablesText.text.isNotEmpty())
            maxSyllablesText.text.toString().toInt()
        else
            DEFAULT_MAX_SYLLABLES

        if(minimum.toString().toInt()>= DEFAULT_MIN_SYLLABLES && maximum >= minimum && maximum<= DEFAULT_MAX_SYLLABLES){
            wordCreator.minSyllables=minimum
            wordCreator.setMaxSyllables(maximum)

            generatedWords.clear()
            generatedWords.addAll(wordCreator.getWords().map { Word(it) } as ArrayList<Word>)
            generatedWords.sortBy { it.toString() }

            fastScroller.wordRecyclerAdapter.notifyDataSetChanged()
        }else{
            Log.d("ERROR","both min and max values have to be between $DEFAULT_MIN_SYLLABLES and $DEFAULT_MAX_SYLLABLES")
            Toast.makeText(this,"Pone valores entre $DEFAULT_MIN_SYLLABLES y $DEFAULT_MAX_SYLLABLES",Toast.LENGTH_SHORT).show()
        }
    }

    fun advancedConfigurationButtonClicked(view: View){
        val advancedConfigurationIntent=Intent(this,CreateWordAdvancedConfigurationActivity::class.java)
        startActivity(advancedConfigurationIntent)
    }

    private fun setupAdapters(){
        val wordRecycleAdapter=findViewById<RecyclerView>(R.id.wordsListView)
        val fastScrollerView=findViewById<FastScrollerView>(R.id.wordsFastscroller)
        val fastScrollerThumbView=findViewById<FastScrollerThumbView>(R.id.wordsFastscroller_thumb)
        textToSpeech=TextToSpeech(this,TextToSpeech.OnInitListener { status ->
            if(status!=TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
            }else{
                Log.d("ERROR","There is a problem with the textToSpeech feature")
            }
        })
        fastScroller=FastScroller(this,wordRecycleAdapter,generatedWords,fastScrollerView,fastScrollerThumbView){ word ->
            textToSpeech.speak(word.toString(),TextToSpeech.QUEUE_FLUSH,null)
        }
    }
}
