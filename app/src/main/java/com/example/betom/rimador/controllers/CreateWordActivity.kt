package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.adapters.FastScroller
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.WordService
import com.example.betom.rimador.utilities.DEFAULT_MAX_SYLLABLES
import com.example.betom.rimador.utilities.DEFAULT_MIN_SYLLABLES
import com.example.betom.rimador.wordHandlers.WordCreator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import kotlinx.android.synthetic.main.activity_create_word.*

class CreateWordActivity : AppCompatActivity() {

    private lateinit var fastScroller:FastScroller

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

            WordService.words.clear()
            WordService.words.addAll(wordCreator.getWords().map { Word(it) } as ArrayList<Word>)
            WordService.words.sortBy { it.toString() }

            fastScroller.wordRecyclerAdapter.notifyDataSetChanged()
        }else{
            Log.d("ERROR","both min and max values have to be between $DEFAULT_MIN_SYLLABLES and $DEFAULT_MAX_SYLLABLES")
            Toast.makeText(this,"Pone valores entre $DEFAULT_MIN_SYLLABLES y $DEFAULT_MAX_SYLLABLES",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapters(){
        val wordRecycleAdapter=findViewById<RecyclerView>(R.id.wordsListView)
        val fastScrollerView=findViewById<FastScrollerView>(R.id.wordsFastscroller)
        val fastScrollerThumbView=findViewById<FastScrollerThumbView>(R.id.wordsFastscroller_thumb)
        fastScroller=FastScroller(this,wordRecycleAdapter,fastScrollerView,fastScrollerThumbView)
    }
}
